package com.steamer.capas.business.facade;

import com.steamer.capas.domain.dto.request.UpdateRequest;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;

import java.util.List;

public interface UserFacade {

    AuthenticationResponse signUp(SignUpRequest request);
    UserDTO getById(String id);
    List<UserDTO> getAll();
    void deleteByUsername(String username);
    UserDTO update(UpdateRequest request, String id);
    AuthenticationResponse login(LoginRequest loginRequest);

}
