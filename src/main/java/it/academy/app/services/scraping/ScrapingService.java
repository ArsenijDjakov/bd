package it.academy.app.services.scraping;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.scraping.*;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.scraping.*;
import it.academy.app.repositories.shop.ShopProductRepository;
import it.academy.app.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapingService {

    @Autowired
    SimilarityService similarityService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ShopProductRepository shopProductRepository;

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

    public void scrape(long shopId, long categoryId) throws IOException {
        WebClient webClient;

        webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<Object> shopProducts = new ArrayList<>();
        switch ((int) shopId) {
            case 1:
                barbora(webClient, categoryId);
                for (Product product : products) {
                    ProductBarbora p = barboraRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
            case 2:
                rimi(webClient, categoryId);
                for (Product product : products) {
                    ProductRimi p = rimiRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
            case 3:
                ciaMarket(webClient, categoryId);
                for (Product product : products) {
                    ProductCiaMarket p = ciaMarketRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
            case 4:
                gruste(webClient, categoryId);
                for (Product product : products) {
                    ProductGruste p = grusteRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
            case 5:
                aibe(webClient, categoryId);
                for (Product product : products) {
                    ProductAibe p = aibeRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
            case 6:
                utenosPrekyba(webClient, categoryId);
                for (Product product : products) {
                    ProductUtenosPrekyba p = utenosPrekybaRepository.findByProductId(product.getId());
                    if (p != null) {
                        shopProducts.add(p);
                    }
                }
                break;
        }
        for (Object product : shopProducts) {
            ProductScratch productScratch = similarityService.getMaxSimilarityScratch(product, shopId, allTitles);
            if (productScratch.getProductId() != 0) {
                long productId = productScratch.getProductId();
                double price = Double.parseDouble(allPrices.get(productScratch.getReceivedProductIndex()));
                productPriceRepository.saveAndFlush(new ProductPrice(productId, shopId,
                        LocalDate.now().toString(), price));
                notificationService.sendNotifications(productId, price);
            }
            addProductLink(shopId, productScratch);
        }
        allTitles = new ArrayList<>();
        allPrices = new ArrayList<>();
        productLinks = new ArrayList<>();
        imageLinks = new ArrayList<>();
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
                page = webClient.getPage("https://www.aibesmaistas.lt/1237-gazuotas-mineralinis-vanduo");
                aibeScrape(page);
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
//            DomNodeList<DomNode> images = page.querySelectorAll("a.b-product--imagelink img");
            DomNodeList<DomNode> links = page.querySelectorAll("a.b-product--imagelink ");

            for (int j = 0; j < prices.size(); j++) {
                allTitles.add(titles.get(j).asText());
                String price = prices.get(j).asText();
                price = price.replace("\u20AC", "");
                price = price.replace(",", ".");
                allPrices.add(price);
//                imageLinks.add(images.get(j).getAttributes().getNamedItem("src").getNodeValue());
//                productLinks.add(links.get(j).getAttributes().getNamedItem("href").getNodeValue());
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
        shopProductRepository.save(shopProduct);
    }
}
