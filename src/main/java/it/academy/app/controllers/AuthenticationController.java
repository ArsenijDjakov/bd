package it.academy.app.controllers;

import it.academy.app.configs.UserAuthenticationProvider;
import it.academy.app.configs.JwtTokenUtil;
import it.academy.app.models.jwt.JwtRequest;
import it.academy.app.models.jwt.JwtResponse;
import it.academy.app.models.user.User;
import it.academy.app.repositories.user.UserRepository;
import it.academy.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    @RequestMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final User user = userRepository.findByUsername(userDetails.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        Date exp = jwtTokenUtil.getExpirationDateFromToken(token);
        return ResponseEntity.ok(new JwtResponse(token, user.getUsername(), user.getEmail(), exp));
    }

    @PostMapping
    @RequestMapping(value = "/logout")
    public Map createAuthenticationToken() {
        return Map.of("message", "success");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}