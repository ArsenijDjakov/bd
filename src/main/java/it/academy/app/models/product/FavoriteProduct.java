package it.academy.app.models.product;

public class FavoriteProduct {

    String name;

    double minPrice;

    String shopLogoLink;

    long productId;

    String shopLink;

    public FavoriteProduct() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public String getShopLogoLink() {
        return shopLogoLink;
    }

    public void setShopLogoLink(String shopLogoLink) {
        this.shopLogoLink = shopLogoLink;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getShopLink() {
        return shopLink;
    }

    public void setShopLink(String shopLink) {
        this.shopLink = shopLink;
    }
}
