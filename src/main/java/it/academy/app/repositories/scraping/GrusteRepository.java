package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductGruste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrusteRepository extends JpaRepository<ProductGruste, Long> {
    List<ProductGruste> findAll();
    ProductGruste findByProductId(long productId);
}
