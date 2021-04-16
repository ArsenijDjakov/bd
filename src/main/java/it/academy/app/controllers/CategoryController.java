package it.academy.app.controllers;

import it.academy.app.models.PagerModel;
import it.academy.app.models.product.Category;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.SubCategory;
import it.academy.app.repositories.category.CategoryRepository;
import it.academy.app.repositories.category.SubCategoryRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class CategoryController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductPaginationService paginationService;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @GetMapping(value = "/category")
    public ModelAndView getAllCategories() {
        List<Category> categories = categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getName)).collect(Collectors.toList());
        return new ModelAndView("categoryGrid")
                .addObject("categories", categories);
    }

    @GetMapping("/category/{categoryId}")
    public ModelAndView getProductsByCategory(@PathVariable("categoryId") long categoryId, @RequestParam("page") Optional<Integer> page,
                                              @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productRepository.findByCategoryId(categoryId).stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));
        PagerModel pager = new PagerModel(productPages.getTotalPages(),productPages.getNumber());
        return new ModelAndView("productGrid")
                .addObject("categoryId", categoryId)
                .addObject("subCategories", subCategories)
                .addObject("productPages", productPages)
                .addObject("pager", pager)
                .addObject("products", products);
    }

    @GetMapping("/category/{categoryId}/{subCategoryId}")
    public ModelAndView getProductsBySubCategory(@PathVariable("categoryId") long categoryId,
                                                 @PathVariable("subCategoryId") long subCategoryId, @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productRepository.findBySubCategoryId(subCategoryId).stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));
        PagerModel pager = new PagerModel(productPages.getTotalPages(),productPages.getNumber());
        return new ModelAndView("productGrid")
                .addObject("subCategoryId", subCategoryId)
                .addObject("productPages", productPages)
                .addObject("pager", pager)
                .addObject("products", products);
    }
}
