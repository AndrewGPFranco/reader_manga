package com.reader.manga.controller;

import com.reader.manga.dto.user.LoginResponseDTO;
import com.reader.manga.dto.user.RecoverUserDTO;
import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.dto.user.UserLoginDTO;
import com.reader.manga.model.User;
import com.reader.manga.service.JwtTokenService;
import com.reader.manga.service.UserMangaService;
import com.reader.manga.service.UserService;
import com.reader.manga.vo.UserMangaVO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserMangaService userMangaService;

    @PostMapping("/register")
    public ResponseEntity<RecoverUserDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        RecoverUserDTO userRegistered = userService.register(userDTO);

        return ResponseEntity.ok().body(userRegistered);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RecoverUserDTO> getUserByEmail(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                userDTO.email(), userDTO.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = jwtTokenService.generateToken((User) auth.getPrincipal());
        Long idUser = ((User) auth.getPrincipal()).getId();

        return ResponseEntity.ok().body(new LoginResponseDTO(userDTO.email(), userDTO.password(), token, idUser));
    }

    @PostMapping("/add-manga")
    public ResponseEntity<String> addMangaToList(@RequestParam Long idManga, @RequestParam Long idUser) {
        userMangaService.adicionaMangaALista(idManga, idUser);
        return ResponseEntity.ok().body("Adicionado na lista");
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

}
