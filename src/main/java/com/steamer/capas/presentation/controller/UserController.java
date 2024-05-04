package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserFacade userFacade;
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable String id){
        return userFacade.getById(id);
    }
    @GetMapping
    public List<UserDTO> getAll() {
        return userFacade.getAll();
    }

    @PostMapping()
    public UserDTO create(@RequestBody SignUpRequest signUpRequest){
        return userFacade.signUp(signUpRequest);
    }

    @PutMapping("/{id}")
    public UserDTO userEdit(@RequestBody SignUpRequest taskRequest,
                            @PathVariable String id){
        return userFacade.update(taskRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id){
        userFacade.deleteById(id);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login method enter userController\n");
        return userFacade.login(loginRequest);
    }

}
