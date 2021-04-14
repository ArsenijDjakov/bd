package it.academy.app.models.scraping;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_name_aibe")
public class ProductAibe implements Serializable {

    private static final long serialVersionUID = -2373243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "productId")
    private long productId;

    @Column(name = "name")
    private String name;

    public ProductAibe() {
    }

    public ProductAibe(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
