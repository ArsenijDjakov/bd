package it.academy.app.controllers;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.PagerModel;
import it.academy.app.models.product.*;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.models.user.User;
import it.academy.app.repositories.category.CategoryRepository;
import it.academy.app.repositories.category.SubCategoryRepository;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.product.ProductReviewRepository;
import it.academy.app.repositories.shop.ShopProductRepository;
import it.academy.app.repositories.shop.ShopRepository;
import it.academy.app.services.ProductPaginationService;
import it.academy.app.services.ProductReviewService;
import it.academy.app.services.UserService;
import it.academy.app.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    ProductReviewService productReviewService;

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ShopProductRepository shopProductRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ProductNotificationRepository productNotificationRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    ProductPaginationService paginationService;

    @GetMapping("/")
    public ModelAndView mainView(@RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productRepository.findAll().stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));
        PagerModel pager = new PagerModel(productPages.getTotalPages(), productPages.getNumber());
        return new ModelAndView("productGrid")
                .addObject("categoryId", 0)
                .addObject("subCategories", subCategories)
                .addObject("productPages", productPages)
                .addObject("pager", pager)
                .addObject("products", products);
    }

    @GetMapping(value = "/product/{productId}")
    public ModelAndView getProductById(@PathVariable("productId") long productId) throws IncorrectDataException {
        Product product = productRepository.findById(productId);
        Category category = categoryRepository.findById(product.getCategory());
        SubCategory subCategory = subCategoryRepository.findById(product.getSubCategoryId());
        List<ProductPrice> productPrices = productPriceRepository.findByProductId(productId);
        List<Shop> shops = shopRepository.findAll();
        List<ShopProduct> shopProduct = shopProductRepository.findByProductId(productId);

        ProductPrice minPrice = null;
        if (!productPrices.isEmpty()) {
            String lastDate = productPrices.stream().max(Comparator.comparing(ProductPrice::getDate)).get().getDate();
            minPrice = productPrices.stream().filter(productPrice -> lastDate.equals(productPrice.getDate()))
                    .min(Comparator.comparing(ProductPrice::getPrice)).get();
            Shop shop = shopRepository.findById(minPrice.getShopId());
            minPrice.setShopName(shop.getName());
            minPrice.setShopLogoLink(shop.getLogoLink());
        }

        List<ProductReview> reviews = productReviewRepository.findByProductId(productId);
        List<ProductReviewEntity> productReviewEntities = new ArrayList<>();
        for (ProductReview productReview : reviews) {
            User user = userService.findById(productReview.getUserId());
            productReviewEntities.add(new ProductReviewEntity(productReview.getText(), productReview.getDate(), user.getUsername()));
        }
        return new ModelAndView("productPage")
                .addObject("product", product)
                .addObject("category", category)
                .addObject("subCategory", subCategory)
                .addObject("shops", shops)
                .addObject("productReviews", productReviewEntities)
                .addObject("minPrice", minPrice)
                .addObject("productPrices", productPrices)
                .addObject("links", shopProduct);
    }

    @PostMapping(value = "/product/{productId}/subscribe")
    @ResponseBody
    public Map addToProductSubscription(@PathVariable("productId") long productId, @RequestBody String email) {
        try {
            emailValidator.checkEmail(email);
            Product product = productRepository.findById(productId);
            List<ProductPrice> productPrices = productPriceRepository.findByProductId(productId);
            ProductPrice productPrice = productPrices.stream().filter(pp -> LocalDate.parse(pp.getDate()).equals(LocalDate.now()))
                    .min(Comparator.comparing(ProductPrice::getPrice)).get();
            List<ProductNotification> productNotifications = productNotificationRepository.findByProductIdAndEmail(productId, email);
            if (productNotifications.size() > 0) {
                return Map.of("message", "alreadySub");
            }
            productNotificationRepository.save(new ProductNotification(product.getId(), email, productPrice.getPrice()));
            return Map.of("message", "success");
        } catch (Exception e) {
            return Map.of("message", "fail");
        }
    }

    @GetMapping("/product")
    public ModelAndView search(@RequestParam String search, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        search = search.replaceAll("[aAąĄcCčČeEęĘėĖiIįĮsSšŠuUųŲūŪzZžŽ]", "_");
        List<Product> products = productRepository.findByNameLike(search);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        Page<Product> productPages = paginationService.findPaginated(products, PageRequest.of(currentPage - 1, pageSize));
        PagerModel pager = new PagerModel(productPages.getTotalPages(), productPages.getNumber());
        return new ModelAndView("productGrid")
                .addObject("categoryId", 0)
                .addObject("productPages", productPages)
                .addObject("pager", pager)
                .addObject("products", products);
    }

    @PostMapping(value = "/product/review/add")
    public Map addToFavorites(Authentication authentication, @RequestBody ProductReview productReview) throws IncorrectDataException {
        User user = userService.findByUsername(authentication.getName());
        productReviewService.addNewReview(user.getId(), productReview);
        return Map.of("message", "success");
    }

}
