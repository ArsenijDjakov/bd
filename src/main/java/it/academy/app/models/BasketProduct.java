package it.academy.app.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
@Table(name = "basketProduct")
public class BasketProduct implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(name = "basketId")
    private long basketId;

    @Column(name = "productId")
    private long productId;

    @Transient
    String productName;

    @Transient
    HashMap<Long, Double> shopPrices;


    public BasketProduct() {
    }

    public BasketProduct(long basketId, long productId) {
        this.basketId = basketId;
        this.productId = productId;
    }

    public BasketProduct(long basketId, long productId, HashMap<Long, Double> shopPrices) {
        this.basketId = basketId;
        this.productId = productId;
        this.shopPrices = shopPrices;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBasketId() {
        return basketId;
    }

    public void setBasketId(long basketId) {
        this.basketId = basketId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public HashMap<Long, Double> getShopPrices() {
        return shopPrices;
    }

    public void setShopPrices(HashMap<Long, Double> shopPrices) {
        this.shopPrices = shopPrices;
    }
}