package services;

import it.academy.app.models.shop.Shop;
import it.academy.app.models.shop.ShopProduct;
import it.academy.app.repositories.shop.ShopProductRepository;
import it.academy.app.repositories.shop.ShopRepository;
import it.academy.app.services.ShopService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShopServiceTest extends TestSetup {

    Shop shop = new Shop();

    ShopProduct shopProduct = new ShopProduct();

    @Mock
    ShopRepository shopRepository;

    @Mock
    ShopProductRepository shopProductRepository;

    @InjectMocks
    ShopService shopService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllShops_returnsShops() {
        List<Shop> result = shopService.getAllShops();
        verify(shopRepository).findAll();
        assertNotNull(result);
    }

    @Test
    public void getShopById_returnsShop() {
        when(shopRepository.findById(SHOP_ID)).thenReturn(shop);
        Shop result = shopService.getShopById(SHOP_ID);
        verify(shopRepository).findById(SHOP_ID);
        assertEquals(shop, result);
    }

    @Test
    public void getShopProductsByProductId_returnsShopProducts() {
        List<ShopProduct> result = shopService.getShopProductsByProductId(PRODUCT_ID);
        verify(shopProductRepository).findByProductId(PRODUCT_ID);
        assertNotNull(result);
    }

    @Test
    public void getByShopIdAndProductId_returnsShopProduct() {
        when(shopProductRepository.findByShopIdAndProductId(SHOP_ID, PRODUCT_ID)).thenReturn(shopProduct);
        ShopProduct result = shopService.getByShopIdAndProductId(SHOP_ID, PRODUCT_ID);
        verify(shopProductRepository).findByShopIdAndProductId(SHOP_ID, PRODUCT_ID);
        assertEquals(shopProduct, result);
    }

    @Test
    public void addNewShopProduct() {
        shopService.addNewShopProduct(shopProduct);
        verify(shopProductRepository).save(shopProduct);
    }
}
