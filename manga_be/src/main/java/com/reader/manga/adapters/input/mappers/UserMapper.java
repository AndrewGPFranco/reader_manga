package com.reader.manga.adapters.input.mappers;

import com.reader.manga.adapters.input.dtos.user.RecoverUserDTO;
import com.reader.manga.adapters.input.dtos.user.UserDTO;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.RoleType;
import org.apache.commons.compress.utils.Sets;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getPassword(), user.getDateBirth());
    }

    public RecoverUserDTO toRecoverDTO(User user) {
        return new RecoverUserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getDateBirth(), user.getUriProfilePhoto(), 0, 0, 0);
    }

    public RecoverUserDTO toRecoverDTO(User user, Integer... data) {
        if (data[0] != null && data[1] != null && data[2] != null) {
            return new RecoverUserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                    user.getEmail(), user.getDateBirth(), user.getUriProfilePhoto(), data[0], data[1], data[2]);
        }
        return new RecoverUserDTO(user.getFirstName(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getDateBirth(), user.getUriProfilePhoto(), 0, 0, 0);
    }

    public User toEntity(UserDTO user) {
        return new User(user.username(), user.fullName(), user.firstName(),
                user.dateBirth(), Sets.newHashSet(RoleType.USER), user.password(), user.email());
    }

}
