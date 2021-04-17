package it.academy.app.controllers;

import it.academy.app.models.ProductPager;
import it.academy.app.models.product.Category;
import it.academy.app.models.product.Product;
import it.academy.app.services.CategoryService;
import it.academy.app.services.product.ProductPaginationService;
import it.academy.app.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductPaginationService paginationService;

    @GetMapping(value = "/category")
    public ModelAndView getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ModelAndView("categoryGrid")
                .addObject("categories", categories);
    }

    @GetMapping("/category/{categoryId}")
    public ModelAndView getProductsByCategory(@PathVariable("categoryId") long categoryId, @RequestParam("page") Optional<Integer> page,
                                              @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(page.orElse(1) - 1, size.orElse(20)));
        return new ModelAndView("productGrid")
                .addObject("categoryId", categoryId)
                .addObject("subCategories", categoryService.getSubCategoriesByCategory(categoryId))
                .addObject("productPages", productPages)
                .addObject("pager", new ProductPager(productPages))
                .addObject("products", products);
    }

    @GetMapping("/category/{categoryId}/{subCategoryId}")
    public ModelAndView getProductsBySubCategory(@PathVariable("categoryId") long categoryId,
                                                 @PathVariable("subCategoryId") long subCategoryId, @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.getProductsBySubCategory(subCategoryId);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(page.orElse(1) - 1, size.orElse(20)));
        return new ModelAndView("productGrid")
                .addObject("subCategoryId", subCategoryId)
                .addObject("productPages", productPages)
                .addObject("pager", new ProductPager(productPages))
                .addObject("products", products);
    }
}
