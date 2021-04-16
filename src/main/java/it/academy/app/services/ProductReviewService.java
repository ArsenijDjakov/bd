package it.academy.app.services;

import it.academy.app.exception.ValidationException;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductReview;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.repositories.product.ProductReviewRepository;
import it.academy.app.validators.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductReviewService {

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductRepository productRepository;

    public void addNewReview(long userId, ProductReview productReview) {
        if(productReview.getText().length()<=256){
            Product product = productRepository.findById(productReview.getProductId());
            productReview.setUserId(userId);
            productReview.setDate(LocalDate.now().toString());
            productReviewRepository.save(productReview);
        } else {
            throw new ValidationException(ErrorMessages.textTooLong);
        }
    }

}
