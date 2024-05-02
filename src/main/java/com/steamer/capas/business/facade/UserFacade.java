package com.steamer.capas.business.facade;

import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.UserRequest;

import java.util.List;

public interface UserFacade {

    UserDTO createNew(UserRequest request);
    UserDTO getById(Long id);
    List<UserDTO> getAll();
<<<<<<< Updated upstream
    void deleteById(Long id);
    UserDTO update(UserRequest request, Long id);
=======
    void deleteById(String id);
    UserDTO update(UserRequest request, String id);
    UserDTO login(LoginRequest loginRequest);
>>>>>>> Stashed changes
}
