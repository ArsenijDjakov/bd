package it.academy.app.models.product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "categoryId")
    private long categoryId;

    @Column(name = "subCategoryId")
    private long subCategoryId;

    @Column(name = "imageLink")
    private String imageLink;

    public Product(String name, long categoryId, long subCategoryId, String imageLink) {
        this.name = name;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.imageLink = imageLink;
    }

    public Product(String name, long categoryId, String imageLink) {
        this.name = name;
        this.categoryId = categoryId;
        this.imageLink = imageLink;
    }

    public Product() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategory() {
        return categoryId;
    }

    public void setCategory(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}