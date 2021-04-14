package it.academy.app.validators;

import it.academy.app.exception.ValidationException;

public abstract class Validator<T> {
    public abstract void validate(T attribute);

    public void doesStringContainLetters(String word) {
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                throw new ValidationException(ErrorMessages.illegalCharactersUsed);
            }
        }
    }
}
