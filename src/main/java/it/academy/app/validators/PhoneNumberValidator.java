package it.academy.app.validators;

import it.academy.app.exception.ValidationException;
import java.util.regex.Pattern;

public class PhoneNumberValidator extends Validator<String> {
    @Override
    public void validate(String attribute) {
        attribute = attribute.trim();

        checkNumberByNumberFormat(attribute);
    }

    private void checkNumberByNumberFormat(String number) {
        String phoneNumberRegex = "^[+]370[0-9]{8}$";
        Pattern pat = Pattern.compile(phoneNumberRegex);

        if (!pat.matcher(number).matches()) {
            throw new ValidationException(ErrorMessages.invalidTelNumberFormat);
        }
    }
}
