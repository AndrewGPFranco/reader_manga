package com.reader.manga.service;

import com.reader.manga.dto.user.RecoverUserDTO;
import com.reader.manga.dto.user.UserDTO;
import com.reader.manga.exception.PasswordException;
import com.reader.manga.mapper.PasswordEncoderMapper;
import com.reader.manga.mapper.UserMapper;
import com.reader.manga.model.User;
import com.reader.manga.repository.UserRepository;
import com.reader.manga.vo.ChangePasswordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
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

    public void changePassword(ChangePasswordVO userVO) {
        User user = repository.findById(userVO.idUser()).orElseThrow(() ->
                new UsernameNotFoundException("Nenhum usuário encontrado com o id: " + userVO.idUser()));

        boolean passwordIsEqual = passwordEncoderMapper.matches(userVO.oldPassword(), user.getPassword());

        if(passwordIsEqual) {
            user.setPassword(passwordEncoderMapper.encode(userVO.newPassword()));
            repository.save(user);
        } else
            throw new PasswordException("A senha informada não corresponde com a atual");
    }

}
