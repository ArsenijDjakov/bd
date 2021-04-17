package it.academy.app.repositories.product;

import it.academy.app.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(long categoryId);
    List<Product> findBySubCategoryId(long subCategoryId);
    Product findById(long id);
    List<Product> findAll();
    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE %:name%")
    List<Product> findByNameLike(@Param("name") String name);
}
