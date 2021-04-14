package validators;

import it.academy.app.exception.ValidationException;
import it.academy.app.shared.Status;
import it.academy.app.validators.UserRegistrationValidator;
import org.junit.Test;

public class UserRegistrationValidatorTest {

    private UserRegistrationValidator userRegistrationValidator = new UserRegistrationValidator();

    @Test(expected = ValidationException.class)
    public void ShouldFailIfStatusNotInProgress(){
        userRegistrationValidator.checkIsStatusInProgress(Status.DECLINED.getStatusInLithuanian());
    }

    @Test
    public void ShouldPassIfStatusInProgress(){
        userRegistrationValidator.checkIsStatusInProgress(Status.INPROGRESS.getStatusInLithuanian());
    }

}
