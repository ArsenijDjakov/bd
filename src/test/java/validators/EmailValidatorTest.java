package validators;

import it.academy.app.validators.EmailValidator;
import org.junit.Test;

public class EmailValidatorTest {
    EmailValidator emailValidator = new EmailValidator();

    @Test(expected = Exception.class)
    public void shouldFailWhenEmailFieldIsEmpty() {
        emailValidator.validate("");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenEmailContainsSpacesOnly() {
        emailValidator.validate("          ");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLocalPartIsMissing() {
        emailValidator.validate("@email.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenDomainIsMissing() {
        emailValidator.validate("smth@.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenTailIsMissing() {
        emailValidator.validate("smth@email");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenDoesNotContainEtSymbol() {
        emailValidator.validate("email.email.email.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenStartsWithDot() {
        emailValidator.validate(".smth@email.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenTwoDotsAppearConsecutively() {
        emailValidator.validate("smth..smth@email.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenContainsSpace() {
        emailValidator.validate("smth. smth@email.com");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenDomainContainsInvalidCharacters() {
        emailValidator.validate("smth@sm+th.com");
    }

    @Test
    public void shouldReturnSuccessWhenEmailIsValid() {
        emailValidator.validate("smth.smth@email.com");
    }
}

