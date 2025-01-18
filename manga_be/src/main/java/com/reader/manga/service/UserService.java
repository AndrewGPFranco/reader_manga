package com.reader.manga.service;

import com.reader.manga.dto.user.RecoverUserDTO;
import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.mapper.PasswordEncoderMapper;
import com.reader.manga.mapper.UserMapper;
import com.reader.manga.model.User;
import com.reader.manga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoderMapper passwordEncoderMapper;
    private final UserMapper userMapper;

    public RecoverUserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoderMapper.encode(user.getPassword()));
        repository.save(user);

        return userMapper.toRecoverDTO(user);
    }

    public RecoverUserDTO getUserByEmail(String email) {
        User user = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Nenhum usuário encontrado com o e-mail: " + email));

        return userMapper.toRecoverDTO(user);
    }

}
