package it.academy.app.services;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.BasketProduct;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.user.UserBasket;
import it.academy.app.repositories.BasketProductRepository;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.services.product.ProductPriceService;
import it.academy.app.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class BasketService {

    @Autowired
    BasketProductRepository basketProductRepository;

    @Autowired
    UserBasketRepository userBasketRepository;

    @Autowired
    ProductPriceService productPriceService;

    @Autowired
    ProductService productService;

    public boolean addProduct(long userId, long productId) throws IncorrectDataException {
        productService.getProductById(productId);
        long basketId = userBasketRepository.findByUserId(userId).getId();
        List<BasketProduct> basketProducts = basketProductRepository.findByBasketId(basketId);
        if (basketProducts.stream().anyMatch(basketProduct -> basketProduct.getProductId() == productId)) {
            return false;
        }
        basketProductRepository.save(new BasketProduct(basketId, productId));
        return true;
    }

    public boolean removeProduct(long userId, long productId) throws IncorrectDataException {
        productService.getProductById(productId);
        long basketId = userBasketRepository.findByUserId(userId).getId();
        BasketProduct basketProduct = basketProductRepository.findByBasketIdAndProductId(basketId, productId);
        if (basketProduct != null) {
            basketProductRepository.delete(basketProduct);
            return true;
        }
        return false;
    }

    public List<BasketProduct> setupPrettyProductLines(long userId, List<Shop> shops) throws IncorrectDataException {
        UserBasket basket = userBasketRepository.findByUserId(userId);
        List<BasketProduct> basketProducts = basketProductRepository.findByBasketId(basket.getId());
        if (!basketProducts.isEmpty()) {
            for (BasketProduct basketProduct : basketProducts) {
                Product product = productService.getProductById(basketProduct.getProductId());
                HashMap<Long, Double> shopPrices = new HashMap<>();
                for (Shop shop : shops) {
                    List<ProductPrice> productPrices = productPriceService.getProductPricesByShopId(basketProduct.getProductId(), shop.getId());
                    if (!productPrices.isEmpty()) {
                        ProductPrice lastProductPrice = productPrices.stream().max(Comparator.comparing(ProductPrice::getDate)).get();
                        shopPrices.put(shop.getId(), lastProductPrice.getPrice());
                    } else {
                        shopPrices.put(shop.getId(), 0.0);
                    }
                }
                basketProduct.setProductName(product.getName());
                basketProduct.setShopPrices(shopPrices);
            }
            return basketProducts;
        }
        return null;
    }

    public HashMap<Long, Double> getTotalSums(List<Shop> shops, List<BasketProduct> products) {
        HashMap<Long, Double> totalSums = new HashMap<>();
        for (Shop currentShop : shops) {
            double sum = 0.0;
            for (BasketProduct product : products) {
                sum += product.getShopPrices().get(currentShop.getId());
            }
            totalSums.put(currentShop.getId(), sum);
        }
        return totalSums;
    }

}
