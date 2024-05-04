package com.steamer.capas.business.facade;

import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;

import java.util.List;

public interface UserFacade {

    UserDTO signUp(SignUpRequest request);
    UserDTO getById(String id);
    List<UserDTO> getAll();
    void deleteById(String id);
    UserDTO update(SignUpRequest request, String id);
    UserDTO login(LoginRequest loginRequest);

}
