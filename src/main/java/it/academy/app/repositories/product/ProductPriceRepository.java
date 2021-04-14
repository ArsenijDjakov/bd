package it.academy.app.repositories.product;

import it.academy.app.models.product.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductId(long productId);
    List<ProductPrice> findByShopIdAndProductId(long shopId, long productId);
    List<ProductPrice> findAll();
}
