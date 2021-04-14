package it.academy.app.models.product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productNotification")
public class ProductNotification implements Serializable {

        private static final long serialVersionUID = -2343243243242432341L;
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        @Column(name = "productId")
        private long productId;

        @Column(name = "email")
        private String email;

        @Column(name = "lastPrice")
        private double lastPrice;

        public ProductNotification(long productId, String email, double lastPrice) {
                this.productId = productId;
                this.email = email;
                this.lastPrice = lastPrice;
        }

        public ProductNotification() {
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public long getProductId() {
                return productId;
        }

        public void setProductId(long productId) {
                this.productId = productId;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public double getLastPrice() {
                return lastPrice;
        }

        public void setLastPrice(double lastPrice) {
                this.lastPrice = lastPrice;
        }
}
