package it.academy.app.services;

import it.academy.app.models.product.Category;
import it.academy.app.models.product.SubCategory;
import it.academy.app.repositories.category.CategoryRepository;
import it.academy.app.repositories.category.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getName)).collect(Collectors.toList());
    }

    public Category findById(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    public List<SubCategory> getSubCategoriesByCategory(long categoryId) {
        return  subCategoryRepository.findByCategoryId(categoryId);
    }

    public SubCategory findSubCategoryById(long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId);
    }

}
