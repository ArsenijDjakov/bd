package it.academy.app.models.product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productReview")
public class ProductReview implements Serializable {

    private static final long serialVersionUID = -2343243243245632341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "productId")
    private long productId;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private String date;

    @Column(name = "userId")
    private long userId;

    @Transient
    private String username;

    public ProductReview() {
    }

    public ProductReview(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public ProductReview(String text, String date, String username) {
        this.text = text;
        this.date = date;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
