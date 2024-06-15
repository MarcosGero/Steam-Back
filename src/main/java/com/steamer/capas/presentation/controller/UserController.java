package com.steamer.capas.presentation.controller;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.service.impl.JwtService;
import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    @PostMapping("/me/carrito/{gameId}")
    public ResponseEntity<String> addToCarrito(@PathVariable String gameId, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);

        boolean added = userFacade.addGameToCarrito(username, gameId);
        if (added) {
            return ResponseEntity.ok("Juego agregado al carrito correctamente.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo agregar el juego al carrito.");
        }
    }
    @GetMapping("/me/carrito")
    public ResponseEntity<List<Game>> getCarrito(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);

        List<Game> carritoGames = userFacade.getCarritoGames(username);
        return ResponseEntity.ok(carritoGames);
    }

    @DeleteMapping("/me/carrito")
    public ResponseEntity<String> clearCarrito(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);

        userFacade.clearCarritoGames(username);
        return ResponseEntity.ok("Carrito vaciado correctamente.");
    }

    @DeleteMapping("/me/carrito/{gameId}")
    public ResponseEntity<String> removeFromCarrito(@PathVariable String gameId, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);

        boolean removed = userFacade.removeGameFromCarrito(username, gameId);
        if (removed) {
            return ResponseEntity.ok("Juego eliminado del carrito correctamente.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo eliminar el juego del carrito.");
        }
    }
    @PostMapping("/{userName}/imagen")
    public ResponseEntity<String> asociarImagen(@PathVariable String userName, @RequestParam("file") MultipartFile file) {
        try {
            userFacade.asociarImagenAlUsuario(userName, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Imagen asociada con éxito.");
    }
    @GetMapping("/me/ownedGames")
    public ResponseEntity<List<Game>> getOwnedGames(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);

        List<Game> ownedGames = userFacade.getOwnedGames(username);
        return ResponseEntity.ok(ownedGames);
    }
    @PostMapping("/me/purchase")
    public ResponseEntity<?> purchase(HttpServletRequest request, @RequestBody Map<String, Double> requestBody) {
        String token = request.getHeader("Authorization").substring(7);
        String username = tokenProvider.extractUsername(token);
        double totalPrice = requestBody.get("totalPrice");

        UserDTO user = userFacade.findByUsername(username);
        if (user.cartera() < totalPrice) {
            return ResponseEntity.badRequest().body("No tienes suficiente dinero en tu cartera.");
        }

        boolean purchaseSuccess = userFacade.purchase(username, totalPrice);
        if (purchaseSuccess) {
            System.out.println("successful purchase");
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.badRequest().body("Error al procesar la compra.");
        }
    }
}
