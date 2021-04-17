package services;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.user.User;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.repositories.user.UserRepository;
import it.academy.app.services.UserService;
import it.academy.app.validators.PasswordValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testSetup.TestSetup;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends TestSetup {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    User user = new User(USERNAME, HASHED_PASSWORD);

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    UserRepository userRepository;

    @Mock
    UserBasketRepository userBasketRepository;

    @InjectMocks
    UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(userRepository.findById(USER_ID)).thenReturn(user);
    }

    @Test
    public void getAllUsers_returnsUsers() {
        List<User> result = userService.getAllUsers();
        verify(userRepository).findAll();
        assertNotNull(result);
    }

    @Test
    public void getByUsername_returnsUser() throws IncorrectDataException {
        User result = userService.getByUsername(USERNAME);
        verify(userRepository).findByUsername(USERNAME);
        assertEquals(user, result);
    }

    @Test
    public void findById_returnsUser() throws IncorrectDataException {
        User result = userService.findById(USER_ID);
        verify(userRepository).findById(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void getUser_returnsUser() throws IncorrectDataException {
        User result = userService.getUser(USERNAME, PASSWORD);
        verify(userRepository).findByUsername(USERNAME);
        assertEquals(user, result);
    }

    @Test
    public void getUser_badPassword() throws IncorrectDataException {
        exceptionRule.expect(IncorrectDataException.class);
        userService.getUser(USERNAME, BAD_PASSWORD);
        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    public void updateUserPassword_whenNewPasswordIsInvalidValid() throws IncorrectDataException {
        when(passwordValidator.checkRegex(BAD_PASSWORD)).thenReturn(false);
        exceptionRule.expect(ValidationException.class);
        userService.updateUserPassword(USERNAME, PASSWORD, BAD_PASSWORD);
    }

    @Test
    public void updateUserPassword_whenNewPasswordIsValid() throws IncorrectDataException {
        when(passwordValidator.checkRegex(NEW_PASSWORD)).thenReturn(true);
        userService.updateUserPassword(USERNAME, PASSWORD, NEW_PASSWORD);
        verify(userRepository).delete(user);
        verify(userRepository).save(user);
    }

    @Test
    public void addNewUser_createsUser() {
        userService.addNewUser(user);
        verify(userRepository).save(any());
        verify(userBasketRepository).save(any());
    }
}
