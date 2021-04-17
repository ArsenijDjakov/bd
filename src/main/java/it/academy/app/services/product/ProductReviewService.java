package it.academy.app.services.product;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.product.ProductReview;
import it.academy.app.models.user.User;
import it.academy.app.repositories.product.ProductReviewRepository;
import it.academy.app.services.UserService;
import it.academy.app.shared.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    public List<ProductReview> getProductReviews(long productId) {
        return productReviewRepository.findByProductId(productId);
    }

    public List<ProductReview> addUsernames(List<ProductReview> reviews) throws IncorrectDataException {
        for (ProductReview productReview : reviews) {
            User user = userService.findById(productReview.getUserId());
            productReview.setUsername(user.getUsername());
        }
        return reviews;
    }

    public void addNewReview(String username, ProductReview productReview) throws IncorrectDataException {
        User user = userService.getByUsername(username);
        if(productReview.getText().length()<=256){
            productService.getProductById(productReview.getProductId());
            productReview.setUserId(user.getId());
            productReview.setDate(LocalDate.now().toString());
            productReviewRepository.save(productReview);
        } else {
            throw new ValidationException(ErrorMessages.textTooLong);
        }
    }

}
