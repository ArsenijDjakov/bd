package it.academy.app.models.shop;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "shopProduct")
public class ShopProduct implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "shopId")
    private long shopId;

    @Column(name = "productId")
    private long productId;

    @Column(name = "productLink")
    private String productLink;

    public ShopProduct() {
    }

    public ShopProduct(long shopId, long productId) {
        this.shopId = shopId;
        this.productId = productId;
        this.productLink = productLink;
    }

    public ShopProduct(long shopId, long productId, String productLink) {
        this.shopId = shopId;
        this.productId = productId;
        this.productLink = productLink;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }
}
