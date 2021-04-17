package services;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.BasketProduct;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.models.shop.Shop;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserBasket;
import it.academy.app.repositories.BasketProductRepository;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.services.BasketService;
import it.academy.app.services.ShopService;
import it.academy.app.services.product.ProductPriceService;
import it.academy.app.services.product.ProductService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BasketServiceTest extends TestSetup {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    User user = new User();

    UserBasket userBasket = new UserBasket();

    BasketProduct basketProduct =  new BasketProduct();

    List<BasketProduct> basketProducts = new ArrayList<>();

    List<ProductPrice> productPrices = new ArrayList<>();

    List<Shop> shops = new ArrayList<>();

    Product product = new Product(PRODUCT_ID, "productName");

    @Mock
    BasketProductRepository basketProductRepository;

    @Mock
    UserBasketRepository userBasketRepository;

    @Mock
    ProductPriceService productPriceService;

    @Mock
    ProductService productService;

    @InjectMocks
    BasketService basketService;

    @Before
    public void init() throws IncorrectDataException {
        userBasket.setId(BASKET_ID);
        setupValidUser();
        setupProductPriceList();
        setupShopList();
        MockitoAnnotations.initMocks(this);
        when(productService.getProductById(PRODUCT_ID)).thenReturn(product);
        when(userBasketRepository.findByUserId(USER_ID)).thenReturn(userBasket);
    }

    @Test
    public void addProduct_requestIsValid() throws IncorrectDataException {
        setupProductList();
        when(basketProductRepository.findByBasketId(BASKET_ID)).thenReturn(basketProducts);
        boolean result = basketService.addProduct(USER_ID, PRODUCT_ID);
        verify(basketProductRepository).save(any());
        assertTrue(result);
    }

    @Test
    public void addProduct_productAlreadyInBasket() throws IncorrectDataException {
        setupProductList();
        basketProducts.add(new BasketProduct(BASKET_ID, PRODUCT_ID));
        when(basketProductRepository.findByBasketId(BASKET_ID)).thenReturn(basketProducts);
        boolean result = basketService.addProduct(USER_ID, PRODUCT_ID);
        verify(basketProductRepository, times(0)).save(any());
        assertFalse(result);
    }

    @Test
    public void removeProduct_requestIsValid() throws IncorrectDataException {
        setupProductList();
        when(basketProductRepository.findByBasketIdAndProductId(BASKET_ID, PRODUCT_ID)).thenReturn(basketProduct);
        boolean result = basketService.removeProduct(USER_ID, PRODUCT_ID);
        verify(basketProductRepository).delete(basketProduct);
        assertTrue(result);
    }

    @Test
    public void addProduct_basketNotFound() throws IncorrectDataException {
        setupProductList();
        when(basketProductRepository.findByBasketIdAndProductId(BASKET_ID, PRODUCT_ID)).thenReturn(null);
        boolean result = basketService.removeProduct(USER_ID, PRODUCT_ID);
        verify(basketProductRepository, times(0)).delete(basketProduct);
        assertFalse(result);
    }

    @Test
    public void setupPrettyProductLines_returnWithProductNameAndPrices() throws IncorrectDataException {
        basketProducts.add(new BasketProduct(BASKET_ID, PRODUCT_ID));
        when(basketProductRepository.findByBasketId(BASKET_ID)).thenReturn(basketProducts);
        when(productPriceService.getProductPricesByShopId(PRODUCT_ID, SHOP_ID)).thenReturn(productPrices);
        List<BasketProduct> result = basketService.setupPrettyProductLines(USER_ID, shops);
        assertNotNull(result.get(0).getProductName());
        assertNotNull(result.get(0).getShopPrices());
    }

    @Test
    public void setupPrettyProductLines_noProductsInBasket() throws IncorrectDataException {
        when(basketProductRepository.findByBasketId(BASKET_ID)).thenReturn(new ArrayList<>());
        when(productPriceService.getProductPricesByShopId(PRODUCT_ID, SHOP_ID)).thenReturn(productPrices);
        List<BasketProduct> result = basketService.setupPrettyProductLines(USER_ID, shops);
        assertNull(result);
    }

    @Test
    public void getTotalSums_returnTotal() {
        HashMap <Long, Double> prices = new HashMap<>();
        prices.put(SHOP_ID, 0.57);
        basketProducts.add(new BasketProduct(BASKET_ID, PRODUCT_ID, prices));
        when(basketProductRepository.findByBasketId(BASKET_ID)).thenReturn(new ArrayList<>());
        when(productPriceService.getProductPricesByShopId(PRODUCT_ID, SHOP_ID)).thenReturn(productPrices);
        HashMap<Long, Double> result = basketService.getTotalSums(shops,basketProducts);
        assertEquals(result.get(SHOP_ID), 0.57, 2);
    }

    private void setupValidUser() {
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
    }

    private void setupProductList(){
        basketProducts.add(new BasketProduct(BASKET_ID, 1));
        basketProducts.add(new BasketProduct(BASKET_ID,2));
        basketProducts.add(new BasketProduct(BASKET_ID,3));
    }

    private void setupProductPriceList(){
        productPrices.add(new ProductPrice(PRODUCT_ID, SHOP_ID, "2021-04-05", 0.99));
        productPrices.add(new ProductPrice(PRODUCT_ID, SHOP_ID, "2021-04-06", 0.99));
        productPrices.add(new ProductPrice(PRODUCT_ID, SHOP_ID, "2021-04-07", 0.99));
    }

    private void setupShopList(){
        shops.add(new Shop(SHOP_ID, "shop"));
    }

}
