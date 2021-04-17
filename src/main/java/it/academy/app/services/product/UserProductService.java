package it.academy.app.services.product;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserProduct;
import it.academy.app.repositories.user.UserProductRepository;
import it.academy.app.services.ShopService;
import it.academy.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserProductService {

    @Autowired
    ProductService productService;

    @Autowired
    ShopService shopService;

    @Autowired
    ProductPriceService productPriceService;

    @Autowired
    UserService userService;

    @Autowired
    UserProductRepository userProductRepository;

    public boolean addNewFavorite(long userId, long productId) throws IncorrectDataException {
        productService.getProductById(productId);
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

    public List<UserProduct> getUserFavoriteProducts(String username) throws IncorrectDataException {
        User user = userService.getByUsername(username);
        List<UserProduct> userProducts = userProductRepository.findByUserId(user.getId());
        for (UserProduct userProduct : userProducts) {
            Product product = productService.getProductById(userProduct.getProductId());
            userProduct.setProductId(product.getId());
            userProduct.setName(product.getName());
            List<ProductPrice> productPrices = productPriceService.getProductPrices(userProduct.getProductId());
            ProductPrice minPrice = productPriceService.getLastMinPrice(productPrices);
            if (minPrice!=null) {
                userProduct.setMinPrice(minPrice.getPrice());
                Shop shop = shopService.getShopById(minPrice.getShopId());
                userProduct.setShopLogoLink(shop.getLogoLink());
                ShopProduct shopProduct = shopService.getByShopIdAndProductId(shop.getId(), product.getId());
                userProduct.setShopLink(shopProduct.getProductLink());
            }
        }
        return userProducts;
    }

}