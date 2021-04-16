package it.academy.app.repositories.product;

import it.academy.app.models.product.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByUserId(long userId);
    List<ProductReview> findByProductId(long productId);
    ProductReview findById(long id);
    List<ProductReview> findAll();
}

