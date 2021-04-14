package it.academy.app.services;

import it.academy.app.models.BasketProduct;
import it.academy.app.models.product.Product;
import it.academy.app.models.user.UserProduct;
import it.academy.app.repositories.BasketProductRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService {

    @Autowired
    BasketProductRepository basketProductRepository;

    @Autowired
    UserBasketRepository userBasketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    public boolean addProduct(long userId, long productId) {
        Product product = productRepository.findById(productId);
        long basketId = userBasketRepository.findByUserId(userId).getId();
        List<BasketProduct> basketProducts = basketProductRepository.findByBasketId(basketId);
        if (basketProducts.stream().anyMatch(basketProduct -> basketProduct.getProductId() == productId)) {
            return false;
        }
        basketProductRepository.save(new BasketProduct(basketId, productId));
        return true;
    }

    public boolean removeProduct(long userId, long productId) {
        Product product = productRepository.findById(productId);
        long basketId = userBasketRepository.findByUserId(userId).getId();
        BasketProduct basketProduct = basketProductRepository.findByBasketIdAndProductId(basketId, productId);
        if (basketProduct != null) {
            basketProductRepository.delete(basketProduct);
            return true;
        }
        return false;
    }

}
