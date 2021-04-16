package it.academy.app.validators;

import it.academy.app.exception.ValidationException;
import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private void checkNumberByNumberFormat(String number) {
        String phoneNumberRegex = "^[+]370[0-9]{8}$";
        Pattern pat = Pattern.compile(phoneNumberRegex);

        if (!pat.matcher(number).matches()) {
            throw new ValidationException(ErrorMessages.invalidTelNumberFormat);
        }
    }
}
