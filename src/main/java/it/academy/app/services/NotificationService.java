package it.academy.app.services;

import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class NotificationService {

    @Autowired
    ProductNotificationRepository productNotificationRepository;

    @Autowired
    ProductRepository productRepository;

    Properties properties = new Properties();

    public NotificationService() throws IOException {
        properties.load(new FileReader("src/main/resources/email/config.properties"));
    }

    public void sendNotifications(long productId, double price) {
        List<ProductNotification> productMailingList = productNotificationRepository.findByProductId(productId);
        for (int i = 0; i < productMailingList.size(); i++) {
            ProductNotification productNotification = productMailingList.get(i);
            if (productNotification.getLastPrice() > price) {
                productNotificationRepository.delete(productNotification);
                productNotificationRepository.save(new ProductNotification(productId, productNotification.getEmail(), price));
                Product notificationProduct = productRepository.findById(productId);
                sendProductPriceChangeNotification(productNotification.getEmail(), productId,
                        notificationProduct.getName(), price);
            }
        }
    }

    private void sendProductPriceChangeNotification(String receiverEmail, long productId, String productName, double price) {

        final String email = Constants.EMAIL;
        final String password = System.getenv("EMAIL_PASS");
        try {
            Session session = Session.getDefaultInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email, password);
                        }
                    });
            Message message = setupPriceChangeNotificationMessage(session, receiverEmail, productId, productName, price);
            Transport.send(message);
        } catch (MessagingException e) {
            e.getMessage();
        }
    }

    private Message setupPriceChangeNotificationMessage(Session session, String receiverEmail, long productId,
                                                       String productName, double price) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Constants.EMAIL));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiverEmail, false));
        message.setSubject(properties.getProperty("message.subject"));
        String text = String.format(properties.getProperty("notification.message"), productName,
                price, productId);
        message.setText(text);
        message.setSentDate(new Date());
        return message;
    }

}
