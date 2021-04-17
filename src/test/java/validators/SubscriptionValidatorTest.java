package validators;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.services.product.ProductService;
import it.academy.app.validators.EmailValidator;
import it.academy.app.validators.SubscriptionValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscriptionValidatorTest extends TestSetup {

    ProductNotification productNotification = new ProductNotification(PRODUCT_ID, EMAIL);

    @Mock
    EmailValidator emailValidator;

    @Mock
    ProductService productService;

    @Mock
    ProductNotificationRepository productNotificationRepository;

    @InjectMocks
    SubscriptionValidator subscriptionValidator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkNewSubscription_newEmail() throws IncorrectDataException {
        when(productNotificationRepository.findByProductIdAndEmail(PRODUCT_ID, EMAIL)).thenReturn(new ArrayList<>());
        boolean result = subscriptionValidator.checkNewSubscription(productNotification);
        verify(emailValidator).checkEmail(EMAIL);
        verify(productService).getProductById(PRODUCT_ID);
        assertTrue(result);
    }

    @Test
    public void checkNewSubscription_alreadySubscribed() throws IncorrectDataException {
        when(productNotificationRepository.findByProductIdAndEmail(PRODUCT_ID, EMAIL)).thenReturn(List.of(new ProductNotification()));
        boolean result = subscriptionValidator.checkNewSubscription(productNotification);
        verify(emailValidator).checkEmail(EMAIL);
        verify(productService).getProductById(PRODUCT_ID);
        assertFalse(result);
    }

}