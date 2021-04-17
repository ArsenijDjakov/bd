package it.academy.app.validators;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.services.product.ProductPriceService;
import it.academy.app.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionValidator {

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    ProductService productService;

    @Autowired
    ProductNotificationRepository productNotificationRepository;

    public boolean checkNewSubscription(ProductNotification productNotification) throws IncorrectDataException {
        long productId = productNotification.getProductId();
        String email = productNotification.getEmail();
        emailValidator.checkEmail(email);
        productService.getProductById(productId);
        List<ProductNotification> productNotifications = productNotificationRepository.findByProductIdAndEmail(productId, email);
        if (productNotifications.size() > 0) {
            return false;
        }
        return true;
    }

}
