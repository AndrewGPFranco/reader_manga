package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.ResponseAPI;
import com.reader.manga.adapters.input.dtos.user.*;
import com.reader.manga.domain.enums.RoleType;
import com.reader.manga.domain.exceptions.PasswordException;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.JwtTokenService;
import com.reader.manga.domain.services.UserMangaService;
import com.reader.manga.domain.services.UserService;
import com.reader.manga.domain.valueobjects.users.ChangePasswordVO;
import com.reader.manga.domain.valueobjects.users.UserMangaVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserMangaService userMangaService;

    @PostMapping("/token")
    public String validateToken(@RequestBody TokenDTO dto) {
        return jwtTokenService.validateToken(dto.token());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            RecoverUserDTO userRegistered = userService.register(userDTO);

            return ResponseEntity.ok().body(userRegistered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<RecoverUserDTO> getUserByEmail(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDTO userDTO) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                    userDTO.email(), userDTO.password());

            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            String token = jwtTokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok().body(new LoginResponseDTO(userDTO.email(), token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-manga")
    public ResponseEntity<String> addMangaToList(@RequestParam Long idManga, @RequestParam Long idUser) {
        userMangaService.adicionaMangaALista(idManga, idUser);
        return ResponseEntity.ok().body("Adicionado na lista");
    }

    @PostMapping("/remove-manga")
    public ResponseEntity<String> removeMangaToList(@RequestParam Long idManga, @RequestParam Long idUser) {
        userMangaService.removeMangaDaLista(idManga, idUser);
        return ResponseEntity.ok().body("Removido da lista");
    }

    @PostMapping("/favorite-manga")
    public ResponseEntity<String> addMangaToFavoriteList(@RequestParam Long idManga, @RequestParam Long idUser) {
        userMangaService.adicionaMangaAListaDeFavoritos(idManga, idUser);
        return ResponseEntity.ok().body("Adicionado na lista de favoritos.");
    }

    @GetMapping("/manga-favorite-list/{idUser}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserMangaVO> getMangasFavoritosDoUsuario(@PathVariable Long idUser) {
        return ResponseEntity.ok().body(userMangaService.getMangasFavoritosDoUsuario(idUser));
    }

    @GetMapping("/manga-list/{idUser}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserMangaVO> getTodosMangasDoUsuario(@PathVariable Long idUser) {
        return ResponseEntity.ok().body(userMangaService.getTodosMangasDoUsuario(idUser));
    }

    @GetMapping("/manga-list-quantity")
    public ResponseEntity<Integer> getQuantidadeTodosMangasDoUsuario(Long idUser) {
        return ResponseEntity.ok().body(userMangaService.getQuantidadeTodosMangasDoUsuario(idUser));
    }

    @PostMapping("/favorite-manga/{idUser}/{idManga}")
    public ResponseEntity<String> changeMangaFavoriteStatus(@PathVariable Long idUser, @PathVariable Long idManga) {
        userMangaService.changeMangaFavoriteStatus(idManga, idUser);
        return ResponseEntity.ok().body("Change made");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordVO userVO) {
        try {
            userService.changePassword(userVO);
            return ResponseEntity.ok().body("Senha alterada com sucesso!");
        } catch (PasswordException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/change-tier/{tier}")
    public ResponseEntity<String> changeTierUser(@PathVariable String tier, @AuthenticationPrincipal User user) {
        try {
            userService.alteraTierDoUsuario(RoleType.valueOf(tier), user);
            return ResponseEntity.ok().body("Mudança realizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/change-photo")
    public ResponseEntity<ResponseAPI> changeProfilePhoto(@AuthenticationPrincipal User user,
                                                          @RequestParam("file") MultipartFile profilePhoto) {
        try {
            userService.handleProfilePhoto(profilePhoto, user);
            return ResponseEntity.ok().body(ResponseAPI.builder().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseAPI(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                            ResponseAPI.builder().message(e.getMessage())
                                    .statusCode(400).build()));
        }
    }

}
