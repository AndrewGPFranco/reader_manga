package com.reader.manga.controller;

import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO userRegistered = userService.register(userDTO);

        return ResponseEntity.ok().body(userRegistered);
    }
}
