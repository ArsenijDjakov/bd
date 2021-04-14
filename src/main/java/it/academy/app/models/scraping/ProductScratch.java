package it.academy.app.models.scraping;

import javax.xml.crypto.Data;
import java.time.LocalDate;

public class ProductScratch {

    long productId;

    int receivedProductIndex;

    public ProductScratch() {
        this.productId = 0;
        this.receivedProductIndex = 0;
    }

    public ProductScratch(long productId, int receivedProductIndex) {
        this.productId = productId;
        this.receivedProductIndex = receivedProductIndex;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getReceivedProductIndex() {
        return receivedProductIndex;
    }

    public void setReceivedProductIndex(int receivedProductIndex) {
        this.receivedProductIndex = receivedProductIndex;
    }
}
