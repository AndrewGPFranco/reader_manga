package com.reader.manga.mapper;

import com.reader.manga.dto.user.RecoverUserDTO;
import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getPassword(), user.getDateBirth());
    }

    public RecoverUserDTO toRecoverDTO(User user) {
        return new RecoverUserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getDateBirth());
    }

    public User toEntity(UserDTO user) {
        return new User(user.username(), user.fullName(), user.firstName(),
                user.dateBirth(), "USER", user.password(), user.email());
    }

}
