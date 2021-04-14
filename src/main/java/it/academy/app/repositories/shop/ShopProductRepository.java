package it.academy.app.repositories.shop;

import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
    List<ShopProduct> findByShopId(long shopId);
    List<ShopProduct> findByProductId(long productId);
    ShopProduct findByShopIdAndProductId(long shopId, long productId);
    List<ShopProduct> findAll();
}
