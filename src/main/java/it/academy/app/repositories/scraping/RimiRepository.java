package it.academy.app.repositories.scraping;

import it.academy.app.models.scraping.ProductRimi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RimiRepository extends JpaRepository<ProductRimi, Long> {
    List<ProductRimi> findAll();
    ProductRimi findByProductId(long productId);
}
