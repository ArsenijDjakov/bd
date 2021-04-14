package it.academy.app.repositories.user;

import it.academy.app.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByUsername(String username);
    List<User> findAll();
    List<User> findByAdminRights(boolean adminRights);
}
