package com.steamer.capas.business.service;

import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService; // Inject here
    private final UserRequestMapper userRequestMapper;
    private final UserService userService;
    public AuthenticationResponse register(SignUpRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new UserException(HttpStatus.FORBIDDEN, "Usuario ya registrado");
        }

        User user = userRequestMapper.toUser(request);  // Convert UserRequest to User
        user.setPassword(passwordEncoderService.hashPassword(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userService.create(user);      // Save User in the database
        var jwtToken = jwtService.generateToken(savedUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
}
