package it.academy.app.repositories.category;

import it.academy.app.models.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
        Category findById(long id);
        List<Category> findAll();
}
