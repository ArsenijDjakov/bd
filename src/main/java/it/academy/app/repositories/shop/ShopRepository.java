package it.academy.app.repositories.shop;

import it.academy.app.models.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findByName(String name);
    Shop findById(long id);
    List<Shop> findAll();
}
