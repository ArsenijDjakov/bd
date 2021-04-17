package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductAibe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AibeRepository extends JpaRepository<ProductAibe, Long> {
    List<ProductAibe> findAll();
    List<Object> findByCategoryId(long categoryId);
    ProductAibe findByProductId(long productId);
}
