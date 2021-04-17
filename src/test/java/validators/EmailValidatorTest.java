package validators;

import it.academy.app.exception.ValidationException;
import it.academy.app.validators.EmailValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

public class EmailValidatorTest extends TestSetup {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @InjectMocks
    EmailValidator emailValidator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkEmail_emailIsValid() {
        emailValidator.checkEmail(EMAIL);
    }

    @Test
    public void checkEmail_emailIsInvalid() {
        exceptionRule.expect(ValidationException.class);
        emailValidator.checkEmail("string");
    }

}

