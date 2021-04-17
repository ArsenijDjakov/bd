package it.academy.app.models.product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productPrice")
public class ProductPrice implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "productId")
    private long productId;

    @Column(name = "shopId")
    private long shopId;

    @Column(name = "date")
    private String date;

    @Column(name = "price")
    private double price;

    @Transient
    private String shopName;

    @Transient
    private String shopLogoLink;

    public ProductPrice() {
    }

    public ProductPrice(long productId, long shopId, String date, double price) {
        this.productId = productId;
        this.shopId = shopId;
        this.date = date;
        this.price = price;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogoLink() {
        return shopLogoLink;
    }

    public void setShopLogoLink(String shopLogoLink) {
        this.shopLogoLink = shopLogoLink;
    }

}
