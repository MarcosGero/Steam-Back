package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.service.impl.JwtService;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserFacade userFacade;

    //todo hacer esto, que pueda cambiar cosas. en este incremento no se requiere
    @PutMapping("/{username}")
    public UserDTO userEdit(@RequestBody UpdateRequest updateRequest,
                            @PathVariable String id){
        return userFacade.update(updateRequest, id);
    }
    @Autowired
    private final JwtService tokenProvider;  // Asumiendo que tienes un proveedor de token JWT

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        // Extraer el token JWT del encabezado de la solicitud
        String token = request.getHeader("Authorization").substring(7);
        String userName = tokenProvider.extractUsername(token);  // Extraer información del usuario del token
        System.out.println(userName);
        // Realizar la operación de eliminación
        boolean isDeleted = userFacade.deleteByUsername(userName);
        if (isDeleted) {
            return ResponseEntity.ok().body("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        // Extraer el token JWT del encabezado de la solicitud
        String token = request.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);  // Extraer el nombre de usuario del token

        // Obtener la información del usuario
        UserDTO user = userFacade.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/me/email")
    public ResponseEntity<String> updateEmail(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);
        String newEmail = request.get("email");

        userFacade.updateEmail(username, newEmail);

        return ResponseEntity.ok("Correo electrónico actualizado correctamente.");
    }
}
