package com.steamer.capas.business.facade;

import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.UserRequest;

import java.util.List;

public interface UserFacade {

    UserDTO createNew(UserRequest request);
    UserDTO getById(String id);
    List<UserDTO> getAll();
    void deleteById(String id);
    UserDTO update(UserRequest request, String id);
    UserDTO login(LoginRequest loginRequest);

}
