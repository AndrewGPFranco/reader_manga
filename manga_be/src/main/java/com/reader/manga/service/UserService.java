package com.reader.manga.service;

import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.mapper.PasswordEncoderMapper;
import com.reader.manga.mapper.UserMapper;
import com.reader.manga.model.User;
import com.reader.manga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderMapper passwordEncoderMapper;
    private final UserMapper userMapper;

    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoderMapper.encode(user.getPassword()));
        userRepository.save(user);

        return userMapper.toDTO(user);
    }
}
