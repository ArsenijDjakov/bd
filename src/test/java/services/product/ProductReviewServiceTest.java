package services.product;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.product.ProductReview;
import it.academy.app.models.user.User;
import it.academy.app.repositories.product.ProductReviewRepository;
import it.academy.app.services.UserService;
import it.academy.app.services.product.ProductReviewService;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductReviewServiceTest extends TestSetup {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    User user = new User(USERNAME, PASSWORD);

    List<ProductReview> reviews = new ArrayList<>();

    ProductReview productReview = new ProductReview();

    @Mock
    ProductReviewRepository productReviewRepository;

    @Mock
    ProductService productService;

    @Mock
    UserService userService;

    @InjectMocks
    ProductReviewService productReviewService;

    @Before
    public void init() throws IncorrectDataException {
        reviews.add(new ProductReview(USER_ID, USERNAME));
        MockitoAnnotations.initMocks(this);
        when(userService.getByUsername(USERNAME)).thenReturn(user);
        when(userService.findById(USER_ID)).thenReturn(user);
    }

    @Test
    public void getProductReviews_returnsProductReviews() {
        List<ProductReview> result = productReviewService.getProductReviews(PRODUCT_ID);
        verify(productReviewRepository).findByProductId(PRODUCT_ID);
        assertNotNull(result);
    }

    @Test
    public void addUsernames_returnsReviewsWithUsername() throws IncorrectDataException {
        List<ProductReview> result = productReviewService.addUsernames(reviews);
        assertEquals(USERNAME, result.get(0).getUsername());
    }

    @Test
    public void addNewReview_reviewIsValid() throws IncorrectDataException {
        setupReview(REVIEW_TEXT);
        productReviewService.addNewReview(USERNAME, productReview);
        verify(productService).getProductById(PRODUCT_ID);
        verify(productReviewRepository).save(productReview);
    }

    @Test
    public void addNewReview_textIsTooLong() throws IncorrectDataException {
        setupReview(TOO_LONG_REVIEW_TEXT);
        exceptionRule.expect(ValidationException.class);
        productReviewService.addNewReview(USERNAME, productReview);
        verify(productReviewRepository, times(0)).save(productReview);
    }

    private void setupReview(String text){
        productReview.setProductId(PRODUCT_ID);
        productReview.setText(text);
    }
}
