package it.academy.app.models.product;

public class ProductReviewEntity {

    private String text;

    private String date;

    private String username;

    public ProductReviewEntity(String text, String date, String username) {
        this.text = text;
        this.date = date;
        this.username = username;
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
