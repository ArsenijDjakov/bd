package it.academy.app.controllers;

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
        ModelAndView modelAndView = new ModelAndView("productGrid");
        List<Product> products = productRepository.findByCategoryId(categoryId);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId);

        Page<Product> productPage = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        return modelAndView
                .addObject("categoryId", categoryId)
                .addObject("subCategories", subCategories)
                .addObject("productPage", productPage)
                .addObject("products", products);
    }

    @GetMapping("/category/{categoryId}/{subCategoryId}")
    public ModelAndView getProductsBySubCategory(@PathVariable("categoryId") long categoryId,
                                                 @PathVariable("subCategoryId") long subCategoryId, @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("size") Optional<Integer> size) {
        ModelAndView modelAndView = new ModelAndView("productGrid");
        List<Product> products = productRepository.findBySubCategoryId(subCategoryId);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);

        Page<Product> productPage = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        return modelAndView
                .addObject("subCategoryId", subCategoryId)
                .addObject("productPage", productPage)
                .addObject("products", products);
    }
}
