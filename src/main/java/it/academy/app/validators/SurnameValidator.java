package it.academy.app.validators;

import it.academy.app.exception.ValidationException;

//Room for improvement: Regex could be used for validation
public class SurnameValidator extends Validator<String> {
    @Override
    public void validate(String attribute) {
        attribute = attribute.trim();

        doesStringContainOnlyLettersSpacesOrDash(attribute);
    }

    public void doesStringContainOnlyLettersSpacesOrDash(String word) {
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if (!(Character.isLetter(c) || c == '-' || c == ' ')) {
                throw new ValidationException(ErrorMessages.illegalCharactersUsed);
            }
        }
    }

}
