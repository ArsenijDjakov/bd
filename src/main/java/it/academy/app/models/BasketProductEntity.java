package it.academy.app.models;

import java.util.HashMap;

public class BasketProductEntity {

    long productId;

    String productName;

    HashMap<Long, Double> shopPrices;

    public BasketProductEntity(long productId, String productName, HashMap<Long, Double> shopPrices) {
        this.productId = productId;
        this.productName = productName;
        this.shopPrices = shopPrices;
    }

    public BasketProductEntity() {
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "BasketProductEntity{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", shopPrices=" + shopPrices +
                '}';
    }
}
