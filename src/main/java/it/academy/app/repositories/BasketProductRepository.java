package it.academy.app.repositories;

import it.academy.app.models.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
    List<BasketProduct> findByBasketId(long basketId);
    BasketProduct findByBasketIdAndProductId(long basketId, long productId);
}
