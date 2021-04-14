package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductCiaMarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CiaMarketRepository extends JpaRepository<ProductCiaMarket, Long> {
    List<ProductCiaMarket> findAll();
    ProductCiaMarket findByProductId(long productId);
}
