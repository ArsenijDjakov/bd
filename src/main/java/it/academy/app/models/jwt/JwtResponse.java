package it.academy.app.models.jwt;

import java.io.Serializable;
import java.util.Date;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private final String username;
    private final String email;
    private final Date exp;

    public JwtResponse(String jwtToken, String username, String email, Date exp) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.email = email;
        this.exp= exp;
    }

    public String getToken() {
        return this.jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getExp() {
        return exp;
    }
}
