package services;

import it.academy.app.models.product.Category;
import it.academy.app.models.product.SubCategory;
import it.academy.app.repositories.category.CategoryRepository;
import it.academy.app.repositories.category.SubCategoryRepository;
import it.academy.app.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTest extends TestSetup {

    Category category = new Category();

    SubCategory subCategory = new SubCategory();

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    SubCategoryRepository subCategoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllCategories_returnsCategories() {
        List<Category> result = categoryService.getAllCategories();
        verify(categoryRepository).findAll();
        assertNotNull(result);
    }

    @Test
    public void findById_returnsCategory() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);
        Category result = categoryService.findById(CATEGORY_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        assertEquals(category, result);
    }

    @Test
    public void getAllSubCategories_returnsSubCategories() {
        List<SubCategory> result = categoryService.getAllSubCategories();
        verify(subCategoryRepository).findAll();
        assertNotNull(result);
    }

    @Test
    public void getSubCategoriesByCategory_returnsSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.add(subCategory);
        when(subCategoryRepository.findByCategoryId(CATEGORY_ID)).thenReturn(subCategories);
        List<SubCategory> result = categoryService.getSubCategoriesByCategory(CATEGORY_ID);
        verify(subCategoryRepository).findByCategoryId(CATEGORY_ID);
        assertEquals(subCategories, result);
    }

    @Test
    public void findSubCategoryById_returnsSubCategory() {
        when(subCategoryRepository.findById(SUBCATEGORY_ID)).thenReturn(subCategory);
        SubCategory result = categoryService.findSubCategoryById(SUBCATEGORY_ID);
        verify(subCategoryRepository).findById(SUBCATEGORY_ID);
        assertEquals(subCategory, result);
    }
}
