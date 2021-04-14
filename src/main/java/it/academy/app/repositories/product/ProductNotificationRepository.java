package it.academy.app.repositories.product;

import it.academy.app.models.product.ProductNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductNotificationRepository extends JpaRepository<ProductNotification, Long> {
    List<ProductNotification> findByProductId(long productId);
    List<ProductNotification> findByEmail(String email);
    List<ProductNotification> findByProductIdAndEmail(long productId, String email);
    List<ProductNotification> findAll();
}
