package it.academy.app.repositories.user;

import it.academy.app.models.user.UserBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBasketRepository extends JpaRepository<UserBasket, Long> {
    List<UserBasket> findAll();
    UserBasket findByUserId(long userId);
}