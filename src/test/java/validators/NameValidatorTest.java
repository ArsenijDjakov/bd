package validators;


import it.academy.app.validators.NameValidator;
import org.junit.Test;

public class NameValidatorTest {
    private NameValidator nameValidator = new NameValidator();

    @Test(expected = Exception.class)
    public void shouldFailWhenStringIsEmpty() {
        nameValidator.validate("");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenStringContainsSpacesOnly() {
        nameValidator.validate("          ");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenContainsNotLettersOnly() {
        nameValidator.validate("A86 gs");
    }
}

