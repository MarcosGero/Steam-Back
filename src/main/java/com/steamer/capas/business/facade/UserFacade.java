package com.steamer.capas.business.facade;

import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.domain.dto.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserFacade {

    void signUp(SignUpRequest request);
    UserDTO getById(String id);
    List<UserDTO> getAll();
    boolean deleteByUsername(String username);
    UserDTO findByUsername(String username);
    void updateEmail(String username, String newEmail);
    UserDTO update(UpdateRequest request, String id);
    AuthenticationResponse login(LoginRequest loginRequest);

    boolean checkAuth(String authToken);
    public boolean addGameToCarrito(String username, String gameId);
    public List<Game> getCarritoGames(String username);
    public void clearCarritoGames(String username);
    public boolean removeGameFromCarrito(String username, String gameId);
    void asociarImagenAlUsuario(String userId, MultipartFile file) throws IOException;

    List<Game> getOwnedGames(String username);
}
