package it.academy.app.validators;

import it.academy.app.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {

    public void checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new ValidationException(ErrorMessages.invalidEmailFormat);
        }
    }
}
