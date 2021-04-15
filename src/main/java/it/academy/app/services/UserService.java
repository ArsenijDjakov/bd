package it.academy.app.services;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.exception.ValidationException;
import it.academy.app.models.user.User;
import it.academy.app.models.user.UserBasket;
import it.academy.app.repositories.user.UserBasketRepository;
import it.academy.app.repositories.user.UserRepository;
import it.academy.app.validators.ErrorMessages;
import it.academy.app.validators.PasswordValidator;
import org.apache.bcel.verifier.statics.Pass1Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordValidator passwordValidator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBasketRepository userBasketRepository;

    public User getUser(String username, String password) throws IncorrectDataException {
        User candidate = findByUsername(username);
        if (candidate ==null) {
            throw new IncorrectDataException("No such user exist");
        }
        if (!BCrypt.checkpw(password, candidate.getPassword())) {
            throw new IncorrectDataException("Incorrect password");
        }
        return candidate;
    }

    public User findByUsername(String username) throws IncorrectDataException {
        return userRepository.findByUsername(username);
    }

    public User updateUserPassword(String username, String oldPassword, String newPassword) throws IncorrectDataException {
        if (passwordValidator.checkRegex(newPassword)) {
            User user = getUser(username, oldPassword);
            userRepository.delete(user);
            String hashedPass = BCrypt.hashpw(newPassword, BCrypt.gensalt(15));
            user.setPassword(hashedPass);
            userRepository.save(user);
            return userRepository.findByUsername(username);
        }
        throw new ValidationException(ErrorMessages.invalidPasswordFormat);
    }

    public void addNewUser(User user) {
        String hashedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(15));
        userRepository.save(new User(user.getUsername(), hashedPass, user.getEmail(), false));
        User createdUser = userRepository.findByUsername(user.getUsername());
        userBasketRepository.save(new UserBasket(createdUser.getId()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user;
        try {
            user = findByUsername(username);
        } catch (IncorrectDataException e) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}