package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductBarbora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarboraRepository extends JpaRepository<ProductBarbora, Long> {
    List<ProductBarbora> findAll();
    ProductBarbora findByProductId(long productId);
}
