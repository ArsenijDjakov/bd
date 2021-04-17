package it.academy.app.models.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userBasket")
public class UserBasket implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "userId")
    private long userId;

    public UserBasket() {
    }

    public UserBasket(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}

