package validators;

import it.academy.app.validators.PhoneNumberValidator;
import org.junit.Test;

public class PhoneNumberValidatorTest {
    private PhoneNumberValidator telNumberValidator = new PhoneNumberValidator();

    @Test(expected = Exception.class)
    public void shouldFailWhenNumberFieldIsEmpty() {
        telNumberValidator.validate("");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenNumberContainsSpacesOnly() {
        telNumberValidator.validate("            ");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatAndIsTooShort() {
        telNumberValidator.validate("+1234567812");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenShortNumberFormatAndIsTooShort() {
        telNumberValidator.validate("81123456");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatAndIsTooLong() {
        telNumberValidator.validate("+123456789012");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenShortNumberFormatAndIsTooLong() {
        telNumberValidator.validate("1234567890");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatAndContainsLetters() {
        telNumberValidator.validate("+123f456k355");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenShortNumberFormatAndContainsLetters() {
        telNumberValidator.validate("01ib345285");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatAndInvalidSymbols() {
        telNumberValidator.validate("+123+-53()34");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenShortNumberFormatAndInvalidSymbols() {
        telNumberValidator.validate("32()+=432");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatDoesNotStartWithPlus() {
        telNumberValidator.validate("123456789076");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenLongNumberFormatIsGiven() {
        telNumberValidator.validate("+3701-23-45678");
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenShortNumberFormatIsGiven() {
        telNumberValidator.validate("861234567");
    }

    @Test
    public void returnSuccessWhenValidFormatIsGiven() {telNumberValidator.validate("+37066666666");}
}
