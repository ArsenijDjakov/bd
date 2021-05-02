package it.academy.app.services.scraping;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import it.academy.app.models.scraping.ProductScratch;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.repositories.scraping.*;
import it.academy.app.services.ShopService;
import it.academy.app.services.EmailService;
import it.academy.app.services.product.ProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScrapingService {

    @Autowired
    SimilarityService similarityService;

    @Autowired
    EmailService emailService;

    @Autowired
    ProductPriceService productPriceService;

    @Autowired
    ShopService shopService;

    @Autowired
    AibeRepository aibeRepository;

    @Autowired
    CiaMarketRepository ciaMarketRepository;

    @Autowired
    BarboraRepository barboraRepository;

    @Autowired
    RimiRepository rimiRepository;

    @Autowired
    GrusteRepository grusteRepository;

    @Autowired
    UtenosPrekybaRepository utenosPrekybaRepository;

    ArrayList<String> allTitles = new ArrayList<>();
    ArrayList<String> allPrices = new ArrayList<>();
    ArrayList<String> productLinks = new ArrayList<>();
    ArrayList<String> imageLinks = new ArrayList<>();

    public void scrape(long categoryId) throws IOException {
        if (productPriceService.checkIsScrapingNeeded()) {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            for (Shop shop : shopService.getAllShops()) {
                long shopId = shop.getId();
                List<Object> shopProducts = new ArrayList<>();
                switch ((int) shopId) {
                    case 1:
                        barbora(webClient, categoryId);
                        shopProducts = barboraRepository.findByCategoryId(categoryId);
                        break;
                    case 2:
                        rimi(webClient, categoryId);
                        shopProducts = rimiRepository.findByCategoryId(categoryId);
                        break;
                    case 3:
                        ciaMarket(webClient, categoryId);
                        shopProducts = ciaMarketRepository.findByCategoryId(categoryId);
                        break;
                    case 4:
                        gruste(webClient, categoryId);
                        shopProducts = grusteRepository.findByCategoryId(categoryId);
                        break;
                    case 5:
                        aibe(webClient, categoryId);
                        shopProducts = aibeRepository.findByCategoryId(categoryId);
                        break;
                    case 6:
                        utenosPrekyba(webClient, categoryId);
                        shopProducts = utenosPrekybaRepository.findByCategoryId(categoryId);
                        break;
                }
                saveScrapedPrices(shopId, shopProducts);
                allTitles = new ArrayList<>();
                allPrices = new ArrayList<>();
                productLinks = new ArrayList<>();
                imageLinks = new ArrayList<>();
            }
            emailService.sendPriceChangeNotifications();
        } else {
            System.out.println("Scraping is not needed... " + new Date());
        }
    }

    private void saveScrapedPrices(long shopId, List<Object> shopProducts) {
        for (Object product : shopProducts) {
            ProductScratch productScratch = similarityService.getMaxSimilarityScratch(product, shopId, allTitles);
            if (productScratch.getProductId() != 0) {
                long productId = productScratch.getProductId();
                double price = Double.parseDouble(allPrices.get(productScratch.getReceivedProductIndex()));
                productPriceService.addNewProductPrice(productId, shopId, price);
            }
            //only for new products
//            addProductLink(shopId, productScratch);
        }
    }

    private void gruste(WebClient webClient, long categoryId) throws IOException {
        int i = 1;
        while (true) {
            System.out.println("started Gruste category:" + categoryId + " page:" + i);
            String category = "";
            switch ((int) categoryId) {
                case 2:
                    category = "darzoves-vaisiai-ir-uogos";
                    break;
                case 3:
                    category = "gerimai";
                    break;
            }
            HtmlPage page = webClient.getPage("https://gruste.lt/eparduotuve/" + category + "?page=" + i);
            DomNodeList<DomNode> titles = page.querySelectorAll("span.pointer");
            DomNodeList<DomNode> prices = page.querySelectorAll("div.border div div.price");
            for (int j = 0; j < titles.size(); j++) {
                allTitles.add(titles.get(j).asText());
                String price = prices.get(j).asText();
                price = price.replace(" \u20AC", "");
                price = price.replace(",", ".");
                if (price.contains(" ")) {
                    price = price.substring(price.indexOf(' '));
                }
                allPrices.add(price);
                productLinks.add(category);
                //only for new products
//                grusteRepository.save(new ProductNameGruste(titles.get(j).asText()));
            }
            i++;
            if (page.querySelectorAll("ul.pagination li[class='next disabled']").size() > 0) {
                break;
            }
        }
    }

    private void ciaMarket(WebClient webClient, long categoryId) throws IOException {
        System.out.println("Started Cia Market category:" + categoryId);
        HtmlPage page;
        switch ((int) categoryId) {
            case 2:
                page = webClient.getPage("https://parduotuve.ciamarket.lt/27-darzoves");
                ciaMarketScrape(page);
                page = webClient.getPage("https://parduotuve.ciamarket.lt/28-vaisiai");
                ciaMarketScrape(page);
                break;
            case 3:
                page = webClient.getPage("https://parduotuve.ciamarket.lt/65-vanduo");
                ciaMarketScrape(page);
                break;
        }
        System.out.println("Finished Cia Market category:" + categoryId);
    }

    private void ciaMarketScrape(HtmlPage page) {
        DomNodeList<DomNode> titles = page.querySelectorAll("p.product-desc a");
        DomNodeList<DomNode> prices = page.querySelectorAll("span.price");
        DomNodeList<DomNode> links = page.querySelectorAll("p.product-desc a");
        for (int j = 0; j < prices.size(); j++) {
            allTitles.add(titles.get(j).asText());
            String price = prices.get(j).asText();
            price = price.replace("\u20AC", "");
            price = price.replace(",", ".");
            allPrices.add(price);
            productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
            //only for new products
//            ciaMarketRepository.save(new ProductNameCiaMarket(titles.get(j).asText()));
        }
    }

    private void aibe(WebClient webClient, long categoryId) throws IOException {
        System.out.println("Started Aibe category:" + categoryId);
        HtmlPage page;
        switch ((int) categoryId) {
            case 2:
                page = webClient.getPage("https://www.aibesmaistas.lt/1245-darzoves");
                aibeScrape(page);
                page = webClient.getPage("https://www.aibesmaistas.lt/1246-vaisiai");
                aibeScrape(page);
                break;
            case 3:
                webClient.getPage("https://www.aibesmaistas.lt/1237-gazuotas-mineralinis-vanduo");
                page = webClient.getPage("https://www.aibesmaistas.lt/1237-gazuotas-mineralinis-vanduo");
                aibeScrape(page);
                webClient.getPage("https://www.aibesmaistas.lt/1238-negazuotas-naturalus-mineralinis-vanduo");
                page = webClient.getPage("https://www.aibesmaistas.lt/1238-negazuotas-naturalus-mineralinis-vanduo");
                aibeScrape(page);
                break;
        }
        System.out.println("Finished Aibe category:" + categoryId);
    }

    private void aibeScrape(HtmlPage page) {
        DomNodeList<DomNode> titles = page.querySelectorAll("span.list-name");
        DomNodeList<DomNode> prices = page.querySelectorAll("span[itemprop='price']");
        DomNodeList<DomNode> links = page.querySelectorAll("a.product-name");
        for (int j = 0; j < prices.size(); j++) {
            allTitles.add(titles.get(j).asText());
            String price = prices.get(j).asText();
            price = price.replace("\u20AC", "");
            price = price.replace(",", ".");
            allPrices.add(price);
            productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
            //only for new products
//            aibeRepository.save(new ProductAibe(titles.get(j).asText()));
        }
    }

    private void barbora(WebClient webClient, long categoryId) throws IOException {
        int i = 1;
        while (true) {
            System.out.println("started Barbora category:" + categoryId + " page:" + i);
            String category = "";
            switch ((int) categoryId) {
                case 2:
                    category = "darzoves-ir-vaisiai";
                    break;
                case 3:
                    category = "gerimai/vanduo";
                    break;
            }
            HtmlPage page = webClient.getPage("https://pagrindinis.barbora.lt/" + category + "?page=" + i);
            if (page.querySelectorAll(".b-product-price-current-number").size() == 0) {
                break;
            }
            DomNodeList<DomNode> titles = page.querySelectorAll("a.b-product-title span[itemprop='name']");
            DomNodeList<DomNode> prices = page.querySelectorAll(".b-product-price-current-number");
            DomNodeList<DomNode> links = page.querySelectorAll("a.b-product--imagelink ");
            //only for new products
//            DomNodeList<DomNode> images = page.querySelectorAll("a.b-product--imagelink img");

            for (int j = 0; j < prices.size(); j++) {
                allTitles.add(titles.get(j).asText());
                String price = prices.get(j).asText();
                price = price.replace("\u20AC", "");
                price = price.replace(",", ".");
                allPrices.add(price);
                //only for new products
//                imageLinks.add(images.get(j).getAttributes().getNamedItem("src").getNodeValue());
                productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
//                productRepository.save(new Product(titles.get(j).asText(), categoryId, "https://barbora.lt" + images.get(j).getAttributes().getNamedItem("src").getNodeValue()));
            }
            i++;
        }
    }

    private void utenosPrekyba(WebClient webClient, long categoryId) throws IOException {
        int i = 1;
        while (true) {
            System.out.println("Started Utenos Prekyba category:" + categoryId + " page:" + i);
            String category = "";
            switch ((int) categoryId) {
                case 2:
                    category = "darzoves-ir-vaisiai";
                    break;
                case 3:
                    category = "nealkoholiniai-gerimai/vanduo";
                    break;
            }
            HtmlPage page = webClient.getPage("https://www.e-utenosprekyba.lt/" + category + "?page=" + i);
            if (page.querySelectorAll("div.price span").size() == 0) {
                break;
            }
            DomNodeList<DomNode> titles = page.querySelectorAll("div.name a span");
            DomNodeList<DomNode> prices = page.querySelectorAll("div.price span");
            DomNodeList<DomNode> links = page.querySelectorAll("div.name a");

            for (int j = 0; j < prices.size(); j++) {
                allTitles.add(titles.get(j).asText());
                String price = prices.get(j).asText();
                price = price.replace("\u20AC", "");
                price = price.replace(",", ".");
                allPrices.add(price);
                productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
                //only for new products
//                utenosPrekybaRepository.save(new ProductUtenosPrekyba(titles.get(j).asText()));
            }
            i++;
        }
    }

    private void rimi(WebClient webClient, long categoryId) throws IOException {
        int i = 1;
        while (true) {
            System.out.println("started Rimi category:" + categoryId + " page:" + i);
            String category = "";
            switch ((int) categoryId) {
                case 2:
                    category = "vaisiai-darzoves-ir-geles/c/SH-15";
                    break;
                case 3:
                    category = "gerimai/vanduo/c/SH-4-10";
                    break;
            }
            HtmlPage page = webClient.getPage("https://www.rimi.lt/e-parduotuve/lt/produktai/" + category + "?page=" + i);
            if (page.querySelectorAll("p.card__name").size() == 0) {
                break;
            }
            DomNodeList<DomNode> titles = page.querySelectorAll("p.card__name");
            DomNodeList<DomNode> pricesF = page.querySelectorAll("div.price-tag span");
            DomNodeList<DomNode> pricesS = page.querySelectorAll("div.price-tag div sup");
            DomNodeList<DomNode> links = page.querySelectorAll("a.card__url");
            for (int j = 0; j < pricesF.size(); j++) {
                allTitles.add(titles.get(j).asText());
                String price = pricesF.get(j).asText() + "." + pricesS.get(j).asText();
                allPrices.add(price);
                productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
                //only for new products
//                rimiRepository.save(new ProductNameRimi(titles.get(j).asText()));
            }
            i++;
        }
    }

    private void addProductLink(long shopId, ProductScratch productScratch) {
        ShopProduct shopProduct = new ShopProduct(shopId, productScratch.getProductId());
        switch ((int) shopId) {
            case 1:
                shopProduct.setProductLink("https://barbora.lt" + productLinks.get(productScratch.getReceivedProductIndex()));
                break;
            case 2:
                shopProduct.setProductLink("https://www.rimi.lt" + productLinks.get(productScratch.getReceivedProductIndex()));
                break;
            case 4:
                shopProduct.setProductLink("https://gruste.lt/eparduotuve/" + productLinks.get(productScratch.getReceivedProductIndex()));
                break;
            case 3:
            case 5:
            case 6:
                shopProduct.setProductLink(productLinks.get(productScratch.getReceivedProductIndex()));
                break;
        }
        shopService.addNewShopProduct(shopProduct);
    }
}
