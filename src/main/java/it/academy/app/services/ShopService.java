package it.academy.app.services;

import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.repositories.shop.ShopProductRepository;
import it.academy.app.repositories.shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ShopProductRepository shopProductRepository;

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Shop getShopById(long shopId){
        return shopRepository.findById(shopId);
    }

    public List<ShopProduct> getShopProductsByProductId(long productId) {
        return shopProductRepository.findByProductId(productId);
    }

    public ShopProduct getByShopIdAndProductId (long shopId, long productId) {
       return shopProductRepository.findByShopIdAndProductId(shopId, productId);
    }

    public void addNewShopProduct(ShopProduct shopProduct) {
        shopProductRepository.save(shopProduct);
    }

}
