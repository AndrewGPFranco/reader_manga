package com.reader.manga.adapters.input.mappers;

import com.reader.manga.adapters.input.dtos.user.RecoverUserDTO;
import com.reader.manga.adapters.input.dtos.user.UserDTO;
import com.reader.manga.domain.entities.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getPassword(), user.getDateBirth());
    }

    public RecoverUserDTO toRecoverDTO(User user, Integer... data) {
        return new RecoverUserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getDateBirth(), data[0], data[1], data[2]);
    }

    public User toEntity(UserDTO user) {
        return new User(user.username(), user.fullName(), user.firstName(),
                user.dateBirth(), "USER", user.password(), user.email());
    }

}
