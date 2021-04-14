package it.academy.app.services;

import it.academy.app.models.product.FavoriteProduct;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserProduct;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.shop.ShopProductRepository;
import it.academy.app.repositories.shop.ShopRepository;
import it.academy.app.repositories.user.UserProductRepository;
import it.academy.app.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class FavoriteProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ShopProductRepository shopProductRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    UserProductRepository userProductRepository;

    @Autowired
    UserRepository userRepository;

    public boolean addNewFavorite(long userId, long productId) {
        Product product = productRepository.findById(productId);
        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
        if (userProduct != null) {
            return false;
        }
        userProductRepository.save(new UserProduct(userId, productId));
        return true;
    }

    public boolean removeFavorite(long userId, long productId) {
        UserProduct userProduct = userProductRepository.findByUserIdAndProductId(userId, productId);
        if (userProduct != null) {
            userProductRepository.delete(userProduct);
            return true;
        }
        return false;
    }

    public ArrayList<FavoriteProduct> getUserFavoriteProducts(String username) {
        User user = userRepository.findByUsername(username);
        List<UserProduct> userProducts = userProductRepository.findByUserId(user.getId());
        ArrayList<FavoriteProduct> favoriteProducts = new ArrayList<>();
        for (int i = 0; i < userProducts.size(); i++) {
            UserProduct userProduct = userProducts.get(i);
            FavoriteProduct favoriteProduct = new FavoriteProduct();
            Product product = productRepository.findById(userProduct.getProductId());
            favoriteProduct.setProductId(product.getId());
            favoriteProduct.setName(product.getName());
            List<ProductPrice> productPrices = productPriceRepository.findByProductId(userProduct.getProductId());
            if (!productPrices.isEmpty()) {
                String lastDate = productPrices.stream().max(Comparator.comparing(ProductPrice::getDate)).get().getDate();
                ProductPrice minPrice = productPrices.stream().filter(productPrice -> lastDate.equals(productPrice.getDate()))
                        .min(Comparator.comparing(ProductPrice::getPrice)).get();
                favoriteProduct.setMinPrice(minPrice.getPrice());
                Shop shop = shopRepository.findById(minPrice.getShopId());
                favoriteProduct.setShopLogoLink(shop.getLogoLink());
                ShopProduct shopProduct = shopProductRepository.findByShopIdAndProductId(shop.getId(), product.getId());
                favoriteProduct.setShopLink(shopProduct.getProductLink());
            }
            favoriteProducts.add(favoriteProduct);
        }
        return favoriteProducts;
    }

}