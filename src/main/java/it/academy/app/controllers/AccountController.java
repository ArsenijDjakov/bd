package it.academy.app.controllers;

import it.academy.app.configs.JwtTokenUtil;
import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.BasketProduct;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserProduct;
import it.academy.app.services.BasketService;
import it.academy.app.services.ShopService;
import it.academy.app.services.UserService;
import it.academy.app.services.product.UserProductService;
import it.academy.app.validators.UserRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    UserRegistrationValidator userRegistrationValidator;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    UserProductService userProductService;

    @Autowired
    BasketService basketService;

    @Autowired
    ShopService shopService;

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
        User user = userService.getByUsername(authentication.getName());
        if (userProductService.addNewFavorite(user.getId(), productId)) {
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
                List<UserProduct> userProducts = userProductService.getUserFavoriteProducts(username);
                ModelAndView modelAndView =  new ModelAndView("favoritesListView");
                if (!userProducts.isEmpty()) {
                    modelAndView.addObject("userProducts", userProducts);
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
        User user = userService.getByUsername(authentication.getName());
        if(userProductService.removeFavorite(user.getId(), productId)) {
            return Map.of("message", "success");
        };
        return Map.of("message", "fail");
    }

    @PostMapping(value = "/account/basket/{productId}")
    public Map addToBasket(Authentication authentication, @PathVariable("productId") long productId) throws IncorrectDataException {
        User user = userService.getByUsername(authentication.getName());
        if (basketService.addProduct(user.getId(), productId)) {
            return Map.of("message", "success");
        }
        return Map.of("message", "alreadyInBasket");
    }

    @PostMapping(value = "/account/basket/remove")
    public Map removeFromBasket(Authentication authentication, long productId) throws IncorrectDataException {
        User user = userService.getByUsername(authentication.getName());
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
                User user = userService.getByUsername(username);
                List<Shop> shops = shopService.getAllShops().stream().sorted(Comparator.comparing(Shop::getName)).collect(Collectors.toList());
                ModelAndView modelAndView = new ModelAndView("basketView");
                List<BasketProduct> basketProducts = basketService.setupPrettyProductLines(user.getId(), shops);
                if (basketProducts!=null) {
                    HashMap<Long, Double> totalSums = basketService.getTotalSums(shops, basketProducts);
                    modelAndView.addObject("totalSums", totalSums);
                }
                return modelAndView.addObject("shops", shops)
                        .addObject("productEntities", basketProducts);
            }
            return new ModelAndView("loginView");
        } catch (Exception e) {
            return new ModelAndView("loginView");
        }
    }

}
