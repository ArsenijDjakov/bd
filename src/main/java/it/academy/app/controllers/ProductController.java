package it.academy.app.controllers;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.ProductPager;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.product.ProductReview;
import it.academy.app.services.*;
import it.academy.app.services.product.*;
import it.academy.app.validators.SubscriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    ProductReviewService productReviewService;

    @Autowired
    ProductNotificationService productNotificationService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductPriceService productPriceService;

    @Autowired
    ShopService shopService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductPaginationService paginationService;

    @Autowired
    SubscriptionValidator subscriptionValidator;

    @GetMapping("/")
    public ModelAndView mainView(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.getAllProduct();
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(page.orElse(1) - 1, size.orElse(20)));
        return new ModelAndView("productGrid")
                .addObject("categoryId", 0)
                .addObject("subCategories", categoryService.getAllSubCategories())
                .addObject("productPages", productPages)
                .addObject("pager", new ProductPager(productPages))
                .addObject("products", products);
    }

    @GetMapping("/faq")
    public ModelAndView faqView() {
        return new ModelAndView("faq");
    }

    @GetMapping(value = "/product/{productId}")
    public ModelAndView getProductById(@PathVariable("productId") long productId) throws IncorrectDataException {
        Product product = productService.getProductById(productId);
        List<ProductPrice> productPrices = productPriceService.getProductPrices(productId);
        List<ProductReview> reviews = productReviewService.getProductReviews(productId);
        return new ModelAndView("productPage")
                .addObject("product", product)
                .addObject("category", categoryService.findById(product.getCategory()))
                .addObject("subCategory", categoryService.findSubCategoryById(product.getSubCategoryId()))
                .addObject("shops", shopService.getAllShops())
                .addObject("productReviews", productReviewService.addUsernames(reviews))
                .addObject("minPrice", productPriceService.getLastMinPrice(productPrices))
                .addObject("productPrices", productPrices)
                .addObject("links", shopService.getShopProductsByProductId(productId));
    }

    @PostMapping(value = "/product/subscribe")
    public Map addToProductSubscription(@RequestBody ProductNotification productNotification) {
        try {
            long productId = productNotification.getProductId();
            List<ProductPrice> productPrices = productPriceService.getProductPrices(productId);
            if (subscriptionValidator.checkNewSubscription(productNotification)) {
                productNotificationService.addNewNotification(productNotification, productPriceService.getLastMinPrice(productPrices).getPrice());
                return Map.of("message", "success");
            }
            return Map.of("message", "alreadySub");
        } catch (Exception e) {
            return Map.of("message", "fail");
        }
    }

    @GetMapping("/product")
    public ModelAndView search(@RequestParam String search, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.searchByName(search);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(page.orElse(1) - 1, size.orElse(20)));
        return new ModelAndView("productGrid")
                .addObject("categoryId", 0)
                .addObject("productPages", productPages)
                .addObject("pager", new ProductPager(productPages))
                .addObject("products", products);
    }

    @PostMapping(value = "/product/review")
    public Map addToFavorites(Authentication authentication, @RequestBody ProductReview productReview) {
        try {
            productReviewService.addNewReview(authentication.getName(), productReview);
            return Map.of("message", "success");
        } catch (IncorrectDataException e) {
            return Map.of("message", "fail");
        }
    }

}
