package validators;

import it.academy.app.exception.ValidationException;
import it.academy.app.models.user.User;
import it.academy.app.services.UserService;
import it.academy.app.validators.EmailValidator;
import it.academy.app.validators.PasswordValidator;
import it.academy.app.validators.UserRegistrationValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRegistrationValidatorTest extends TestSetup {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    User user;

    @Mock
    EmailValidator emailValidator;

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    UserService userService;

    @InjectMocks
    UserRegistrationValidator userRegistrationValidator;

    @Before
    public void init() {
        user = setupValidForm(USERNAME, EMAIL);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkForm_formIsValid(){
        when(passwordValidator.checkRegex(PASSWORD)).thenReturn(true);
        when(userService.getAllUsers()).thenReturn(setupUserList());
        userRegistrationValidator.checkForm(user);
        verify(emailValidator).checkEmail(EMAIL);
    }

    @Test
    public void checkForm_invalidUsername() {
        user.setUsername("1 2");
        when(passwordValidator.checkRegex(PASSWORD)).thenReturn(true);
        when(userService.getAllUsers()).thenReturn(setupUserList());
        exceptionRule.expect(ValidationException.class);
        userRegistrationValidator.checkForm(user);
    }

    @Test
    public void checkForm_passwordIsNull() {
        user.setPassword(null);
        when(userService.getAllUsers()).thenReturn(setupUserList());
        exceptionRule.expect(ValidationException.class);
        userRegistrationValidator.checkForm(user);
    }

    @Test
    public void checkForm_usernameAlreadyExist() {
        when(passwordValidator.checkRegex(PASSWORD)).thenReturn(true);
        List<User> users = setupUserList();
        users.add(new User(USERNAME, "pass", "email", false));
        when(userService.getAllUsers()).thenReturn(users);
        exceptionRule.expect(ValidationException.class);
        userRegistrationValidator.checkForm(user);
    }

    @Test
    public void checkForm_emailAlreadyExist() {
        when(passwordValidator.checkRegex(PASSWORD)).thenReturn(true);
        List<User> users = setupUserList();
        users.add(new User("userUser", "pass", EMAIL, false));
        when(userService.getAllUsers()).thenReturn(users);
        exceptionRule.expect(ValidationException.class);
        userRegistrationValidator.checkForm(user);
    }

    private User setupValidForm(String username, String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(PASSWORD);
        return user;
    }

    private List<User> setupUserList(){
        List<User> fakeUsers = new ArrayList<>();
        fakeUsers.add(setupValidForm("user1", "ee1@ee.ee"));
        fakeUsers.add(setupValidForm("user2","ee2@ee.ee"));
        fakeUsers.add(setupValidForm("user3","ee3@ee.ee"));
        return  fakeUsers;
    }

}