package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.business.service.EmailSender;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.ConfirmationToken;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final EmailSender emailSender;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidatorService emailValidator;
    public String register(SignUpRequest request) {

        boolean isValidEmail = emailValidator.
                test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = signUp(
                new User(
                        request.getUserName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getCountry()
                )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getUserName(), link));

        return token;
    }

    public String signUp(User user){
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();
        if (userExists) {
            throw new IllegalStateException("email already taken");
        }
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new UserException(HttpStatus.FORBIDDEN, "Usuario ya registrado");
        }
        user.setPassword(passwordEncoderService.hashPassword(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userService.create(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().plusMinutes(15),
                savedUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        confirmationTokenService.setConfirmedAt(token);
        // !!!
        // appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }

    public boolean isAuthenticated(String authToken){
        return jwtService.isTokenValid(authToken);
    }
    public AuthenticationResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("username:"+ username);
        System.out.println("password:"+ password);

        User user = userRepository.findByUserName(username); // Busca por email
        if (user == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }


        if (!passwordEncoderService.matches(user.getPassword(),password)) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String buildEmail(String name, String link) {
        return ""; // Poner formato del mail
    }
}
