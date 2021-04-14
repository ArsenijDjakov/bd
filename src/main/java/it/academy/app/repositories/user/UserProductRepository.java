package it.academy.app.repositories.user;

import it.academy.app.models.user.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
    List<UserProduct> findByUserId(long userId);
    UserProduct findByUserIdAndProductId(long userId, long productId);
}
