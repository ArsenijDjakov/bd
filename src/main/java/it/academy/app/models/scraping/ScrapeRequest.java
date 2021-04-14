package it.academy.app.models.scraping;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ScrapeRequest {

    @NotNull
    int shopId;

    @NotNull
    int categoryId;

    public ScrapeRequest() {
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
