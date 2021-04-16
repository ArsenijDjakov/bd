package it.academy.app.validators;

import it.academy.app.exception.ValidationException;
import it.academy.app.models.user.User;
import it.academy.app.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRegistrationValidator {

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    PasswordValidator passwordValidator;

    @Autowired
    UserRepository userRepository;

    public boolean checkForm(User user) {
        checkUsername(user.getUsername());
        checkPassword(user.getPassword());
        emailValidator.checkEmail(user.getEmail());
        List<User> users = userRepository.findAll();
        if (users.stream().anyMatch(su -> su.getUsername().equals(user.getUsername()))) {
            throw new ValidationException(ErrorMessages.usernameAlreadyExist);
        }
        if (users.stream().anyMatch(su -> su.getEmail().equals(user.getEmail()))) {
            throw new ValidationException(ErrorMessages.emailAlreadyExist);
        }
        return true;
    }

    private void checkUsername (String username) {
        if (username==null || username.isEmpty() || username.contains(" ")) {
            throw new ValidationException(ErrorMessages.invalidUsernameFormat);
        }
    }

    private void checkPassword (String password) {
        if (password==null || !passwordValidator.checkRegex(password)) {
            throw new ValidationException(ErrorMessages.invalidPasswordFormat);
        }
    }

}
