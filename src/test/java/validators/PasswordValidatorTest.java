package validators;

import it.academy.app.validators.PasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordValidatorTest extends TestSetup {

    @InjectMocks
    PasswordValidator passwordValidator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkRegex_passwordIsValid() {
        boolean result = passwordValidator.checkRegex(PASSWORD);
        assertTrue(result);
    }

    @Test
    public void checkRegex_passwordIsInvalid() {
        boolean result = passwordValidator.checkRegex(BAD_PASSWORD);
        assertFalse(result);
    }

}