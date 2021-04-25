package it.academy.app.services.product;

import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductPriceService {

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ShopRepository shopRepository;

    public List<ProductPrice> getProductPrices(long productId) {
        return productPriceRepository.findByProductId(productId);
    }

    public List<ProductPrice> getProductPricesByShopId(long productId, long shopId) {
        return productPriceRepository.findByShopIdAndProductId(shopId, productId);
    }

    public ProductPrice getLastMinPrice(List<ProductPrice> productPrices) {
        ProductPrice minPrice = null;
        if (!productPrices.isEmpty()) {
            String lastDate = productPrices.stream().max(Comparator.comparing(ProductPrice::getDate)).get().getDate();
            minPrice = productPrices.stream().filter(productPrice -> lastDate.equals(productPrice.getDate()))
                    .min(Comparator.comparing(ProductPrice::getPrice)).get();
            Shop shop = shopRepository.findById(minPrice.getShopId());
            minPrice.setShopName(shop.getName());
            minPrice.setShopLogoLink(shop.getLogoLink());
        }
        return minPrice;
    }

    public void addNewProductPrice(long productId, long shopId, double price) {
        productPriceRepository.save(new ProductPrice(productId, shopId,
                LocalDate.now().toString(), price));
    }

    public boolean checkIsScrapingNeeded() {
        List<ProductPrice> todayProductPrices = productPriceRepository.findAllByDate(LocalDate.now().toString());
        return todayProductPrices.isEmpty();
    }

}
