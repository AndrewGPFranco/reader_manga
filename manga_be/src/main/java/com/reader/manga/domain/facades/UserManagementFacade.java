package com.reader.manga.domain.facades;

import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.ports.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserManagementFacade {

    private final UserRepository userRepository;

    public User getUserById(Long idUser) {
        return userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException(String.format("Nenhum usuário com o ID: %s encontrado!", idUser)));
    }

    public String getUriProfilePhoto(Long idUser) {
        return getUserById(idUser).getUriProfilePhoto();
    }
}
