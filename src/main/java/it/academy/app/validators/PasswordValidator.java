package it.academy.app.validators;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PasswordValidator {

    public boolean checkRegex(String password) {
        String emailRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,10}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(password).matches()) {
           return false;
        }
        return true;
    }

}
