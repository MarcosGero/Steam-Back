package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.service.impl.AuthenticationService;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserFacade userFacade;
    private final AuthenticationService authenticationService;

    @GetMapping("/check")
    public String checkAuth(@RequestHeader("Authorization") String authToken) {
        boolean isAuthenticated = userFacade.checkAuth(authToken);
        return isAuthenticated ? "User is authenticated" : "User is not authenticated";
    }
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable String id){
        return userFacade.getById(id);
    }
    @GetMapping
    public List<UserDTO> getAll() {
        return userFacade.getAll();
    }

    @PostMapping("/signup")
    public void signUp(
            @RequestBody SignUpRequest signUpRequest){
        userFacade.signUp(signUpRequest);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam String token, HttpServletResponse response) {
        return authenticationService.confirmToken(token,response);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login method enter userController\n");
        return userFacade.login(loginRequest);
    }
    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        authenticationService.createPasswordResetToken(email);
        return ResponseEntity.ok("Solicitud de recuperación de contraseña enviada.");
    }

    @PostMapping("/password-reset/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");
        authenticationService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Contraseña restablecida correctamente.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authToken) {
        boolean isLoggedOut = authenticationService.logout(authToken);
        if (isLoggedOut) {
            return ResponseEntity.ok("User logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
