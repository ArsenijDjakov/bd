package it.academy.app.services;

import it.academy.app.models.product.Product;
import it.academy.app.models.product.ProductNotification;
import it.academy.app.models.product.ProductPrice;
import it.academy.app.repositories.product.ProductNotificationRepository;
import it.academy.app.repositories.product.ProductPriceRepository;
import it.academy.app.repositories.product.ProductRepository;
import it.academy.app.services.product.ProductPriceService;
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
public class EmailService {

    final String EMAIL = "price.monitoring.system.bd@gmail.com";
    final String password = System.getenv("EMAIL_PASS");

    @Autowired
    ProductPriceService productPriceService;

    @Autowired
    ProductNotificationRepository productNotificationRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    Properties properties = new Properties();

    public EmailService() throws IOException {
        properties.load(new FileReader("src/main/resources/email/config.properties"));
    }

    public void addNewNotification(ProductNotification productNotification, double price) {
        productNotification.setLastPrice(price);
        productNotificationRepository.save(productNotification);
    }

    public void sendPriceChangeNotifications() {
        List<ProductNotification> productMailingList = productNotificationRepository.findAll();
        for (ProductNotification productNotification : productMailingList) {
            long productId = productNotification.getProductId();
            List<ProductPrice> prices = productPriceRepository.findByProductId(productId);
            ProductPrice lastMinPrice = productPriceService.getLastMinPrice(prices);
            if (productNotification.getLastPrice() != lastMinPrice.getPrice()) {
                productNotificationRepository.delete(productNotification);
                productNotificationRepository.save(new ProductNotification(productId, productNotification.getEmail(), lastMinPrice.getPrice()));
                Product notificationProduct = productRepository.findById(productId);
                String emailText = String.format(properties.getProperty("notification.message"), notificationProduct.getName(),
                        lastMinPrice.getPrice(), productId);
                sendEmail(productNotification.getEmail(), properties.getProperty("notification.message.subject"), emailText);
            }
        }
    }

    public void sendRegistrationConfirmation(String receiverEmail, String username, String password) {
        String passwordAlias = password.charAt(0) +
                "*".repeat(password.length()-2) +
                password.charAt(password.length() - 1);
        String text = String.format(properties.getProperty("registration.message"), username,
                passwordAlias);
        sendEmail(receiverEmail, properties.getProperty("registration.message.subject"), text);
    }

    private void sendEmail(String receiverEmail, String emailSubject, String emailText) {
        try {
            Session session = Session.getDefaultInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(EMAIL, password);
                        }
                    });
            Message message = setupMessage(session, receiverEmail, emailSubject, emailText);
            Transport.send(message);
        } catch (MessagingException e) {
            e.getMessage();
        }
    }

    private Message setupMessage(Session session, String receiverEmail, String emailSubject, String emailText) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiverEmail, false));
        message.setSubject(emailSubject);
        message.setText(emailText);
        message.setSentDate(new Date());
        return message;
    }
}
