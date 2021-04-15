package it.academy.app.controllers;

import it.academy.app.configs.JwtTokenUtil;
import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.BasketProduct;
import it.academy.app.models.BasketProductEntity;
import it.academy.app.models.product.FavoriteProduct;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserBasket;
import it.academy.app.repositories.BasketProductRepository;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.shop.ShopRepository;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.repositories.user.UserProductRepository;
import it.academy.app.repositories.user.UserRepository;
import it.academy.app.services.BasketService;
import it.academy.app.services.FavoriteProductService;
import it.academy.app.services.UserService;
import it.academy.app.validators.UserRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    UserRegistrationValidator userRegistrationValidator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    UserBasketRepository userBasketRepository;

    @Autowired
    BasketProductRepository basketProductRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserProductRepository userProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FavoriteProductService favoriteProductService;

    @Autowired
    BasketService basketService;

    @Autowired
    ShopRepository shopRepository;

    @GetMapping("/login")
    public ModelAndView loginView() {
        return new ModelAndView("loginView");
    }

    @GetMapping(value = "/registration")
    public ModelAndView registrationView() {
        return new ModelAndView("registrationView");
    }

    @PostMapping(value = "/registration")
    @ResponseBody
    public Map addNewUser(@RequestBody User user) {
        userRegistrationValidator.checkForm(user);
        userService.addNewUser(user);
        return Map.of("message", "success");
    }

    @GetMapping(value = "/account/password/{token:.+}")
    public ModelAndView changePasswordView(@PathVariable("token") String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                return new ModelAndView("changePasswordView");
            } else {
                return new ModelAndView("loginView");
            }
        } catch (Exception e) {
            return new ModelAndView("loginView");
        }
    }

    @PostMapping(value = "/account/password")
    @ResponseBody
    public Map changePassword(Authentication authentication, String oldPassword, String newPassword) throws IncorrectDataException {
        String username = authentication.getName();
        userService.updateUserPassword(username, oldPassword, newPassword);
        return Map.of("message", "success");
    }

    @PostMapping(value = "/account/favorites/{productId}")
    public Map addToFavorites(Authentication authentication, @PathVariable("productId") long productId) throws IncorrectDataException {
        User user = userService.findByUsername(authentication.getName());
        if (favoriteProductService.addNewFavorite(user.getId(), productId)) {
            return Map.of("message", "success");
        }
        return Map.of("message", "alreadyInFavorites");
    }

    @GetMapping(value = "/account/favorites/{token:.+}")
    public ModelAndView favoritesListView(@PathVariable("token") String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                ArrayList<FavoriteProduct> favoriteProducts = favoriteProductService.getUserFavoriteProducts(username);
                ModelAndView modelAndView =  new ModelAndView("favoritesListView");
                if (!favoriteProducts.isEmpty()) {
                    modelAndView.addObject("userProducts", favoriteProducts);
                } else {
                    modelAndView.addObject("userProducts", null);
                }
                return modelAndView;
            }
            return new ModelAndView("loginView");
        } catch (Exception e) {
            return new ModelAndView("loginView");
        }
    }

    @PostMapping(value = "/account/favorites/remove")
    public Map removeFromFavorites(Authentication authentication, long productId) throws IncorrectDataException {
        User user = userService.findByUsername(authentication.getName());
        if(favoriteProductService.removeFavorite(user.getId(), productId)) {
            return Map.of("message", "success");
        };
        return Map.of("message", "fail");
    }

    @PostMapping(value = "/account/basket/{productId}")
    public Map addToBasket(Authentication authentication, @PathVariable("productId") long productId) throws IncorrectDataException {
        User user = userService.findByUsername(authentication.getName());
        if (basketService.addProduct(user.getId(), productId)) {
            return Map.of("message", "success");
        }
        return Map.of("message", "alreadyInBasket");
    }

    @PostMapping(value = "/account/basket/remove")
    public Map removeFromBasket(Authentication authentication, long productId) throws IncorrectDataException {
        User user = userService.findByUsername(authentication.getName());
        if(basketService.removeProduct(user.getId(), productId)) {
            return Map.of("message", "success");
        };
        return Map.of("message", "fail");
    }

    @GetMapping(value = "/account/basket/{token:.+}")
    public ModelAndView basketView(@PathVariable("token") String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                User user = userRepository.findByUsername(username);
                UserBasket basket = userBasketRepository.findByUserId(user.getId());
                List<Shop> shops = shopRepository.findAll().stream().sorted(Comparator.comparing(Shop::getName)).collect(Collectors.toList());
                List<BasketProduct> basketProducts = basketProductRepository.findByBasketId(basket.getId());
                ModelAndView modelAndView = new ModelAndView("basketView");
                if (!basketProducts.isEmpty()) {
                    ArrayList<BasketProductEntity> productEntities = new ArrayList<>();
                    for (int i=0;i<basketProducts.size();i++) {
                        BasketProduct basketProduct = basketProducts.get(i);
                        Product product = productRepository.findById(basketProduct.getProductId());
                        HashMap<Long, Double> shopPrices = new HashMap<>();
                        for(int j=0;j<shops.size();j++) {
                            Shop shop = shops.get(j);
                            List<ProductPrice> productPrices = productPriceRepository
                                    .findByShopIdAndProductId(shop.getId(), basketProduct.getProductId());
                            if (!productPrices.isEmpty()) {
                                ProductPrice lastProductPrice = productPrices.stream().max(Comparator.comparing(ProductPrice::getDate)).get();
                                shopPrices.put(shop.getId(), lastProductPrice.getPrice());
                            } else {
                                shopPrices.put(shop.getId(), 0.0);
                            }
                        }
                        productEntities.add(new BasketProductEntity(basketProduct.getProductId(), product.getName(),
                                shopPrices));
                    }
                    HashMap<Long, Double> totalSums = new HashMap<>();
                    for (int i=0;i<shops.size();i++) {
                        Shop currentShop = shops.get(i);
                        double sum = 0.0;
                        for (BasketProductEntity productEntity : productEntities) {
                            sum += productEntity.getShopPrices().get(currentShop.getId());
                        }
                        totalSums.put(shops.get(i).getId(), sum);
                    }
                    modelAndView.addObject("productEntities", productEntities)
                    .addObject("totalSums", totalSums);
                } else {
                    modelAndView.addObject("productEntities", null);
                }
                return  modelAndView.addObject("shops", shops);
            }
            return new ModelAndView("loginView");
        } catch (Exception e) {
            return new ModelAndView("loginView");
        }
    }

}
