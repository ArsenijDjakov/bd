package it.academy.app.repositories.category;

import it.academy.app.models.product.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findById(long id);
    List<SubCategory> findAll();
    List<SubCategory> findByCategoryId(long categoryId);
}

