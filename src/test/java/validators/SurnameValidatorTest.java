package validators;

import it.academy.app.validators.SurnameValidator;
import org.junit.Test;

public class SurnameValidatorTest {
    private SurnameValidator surnameValidator = new SurnameValidator();

    @Test(expected = Exception.class)
    public void shouldFailWhenInputIsEmpty() {
        surnameValidator.validate("");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenInputContainsSpacesOnly() {
        surnameValidator.validate("             ");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenInputContainsMoreThanTwoSurnames() {
        surnameValidator.validate("Kazkas Kazkokyte Kazkauskiene Kazkeliauskas");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenSurnamesContainsNotLettersOrDash() {
        surnameValidator.validate("Miau-mia0          ");
    }

    @Test
    public void shouldSuccessWhenTwoSurnamesAreSeparatedByDash() {
        surnameValidator.validate("Kazkokyte-Kazkauskiene");
    }

    @Test
    public void shouldReturnSuccessWhenSurnameIsValid() {
        surnameValidator.validate("Kazkokyte");
    }
}
