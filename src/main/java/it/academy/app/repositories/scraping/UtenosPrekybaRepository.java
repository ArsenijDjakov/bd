package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductAibe;
import it.academy.app.models.scraping.ProductUtenosPrekyba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenosPrekybaRepository extends JpaRepository<ProductUtenosPrekyba, Long> {
    List<ProductUtenosPrekyba> findAll();
    List<Object> findByCategoryId(long categoryId);
    ProductUtenosPrekyba findByProductId(long productId);
}
