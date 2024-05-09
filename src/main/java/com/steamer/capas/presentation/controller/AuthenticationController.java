package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserFacade userFacade;

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

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login method enter userController\n");
        return userFacade.login(loginRequest);
    }
    //Quiza a futuro nos interese implementar esto del lado de back
    //@GetMapping("/logout")
    //public AuthenticationResponse logout(@RequestHeader("Authorization") String authToken) {
    //    ...
    //}
}
