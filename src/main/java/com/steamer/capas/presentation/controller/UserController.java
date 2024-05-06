package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserFacade userFacade;

    //todo hacer esto, que pueda cambiar cosas. en este incremento no se requiere
    @PutMapping("/{id}")
    public UserDTO userEdit(@RequestBody UpdateRequest updateRequest,
                            @PathVariable String id){
        return userFacade.update(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id){
        userFacade.deleteByUsername(id);
    }
}
