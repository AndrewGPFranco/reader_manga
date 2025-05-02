package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.user.RecoverUserDTO;
import com.reader.manga.adapters.input.dtos.user.UserDTO;
import com.reader.manga.domain.enums.RoleType;
import com.reader.manga.domain.exceptions.PasswordException;
import com.reader.manga.adapters.input.mappers.PasswordEncoderMapper;
import com.reader.manga.adapters.input.mappers.UserMapper;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.ports.repositories.UserChapterRepository;
import com.reader.manga.ports.repositories.UserMangaRepository;
import com.reader.manga.ports.repositories.UserRepository;
import com.reader.manga.domain.valueobjects.users.ChangePasswordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final UserMangaRepository userMangaRepository;
    private final PasswordEncoderMapper passwordEncoderMapper;
    private final UserChapterRepository userChapterRepository;

    public RecoverUserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoderMapper.encode(user.getPassword()));
        repository.save(user);

        return userMapper.toRecoverDTO(user);
    }

    public void alteraTierDoUsuario(RoleType role, User user) {
        switch (role) {
            case USER -> user.setRoles(Sets.newHashSet(RoleType.USER));
            case MODERATOR -> user.setRoles(Sets.newHashSet(RoleType.USER, RoleType.MODERATOR));
            case ADMIN -> user.setRoles(Sets.newHashSet(RoleType.USER, RoleType.MODERATOR, RoleType.ADMIN));
        }

        repository.save(user);
    }

    public RecoverUserDTO getUserByEmail(String email) {
        User user = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Nenhum usuário encontrado com o e-mail: " + email));

        Integer mangas = userMangaRepository.quantidadeMangasAssinadosPeloUsuario(user.getId());
        Integer completeReadings = userChapterRepository.getQuantidadeDeLeiturasFinalizadas(user.getId());
        Integer inProgressReadings = userChapterRepository.getQuantidadeDeLeiturasEmAndamento(user.getId());

        return userMapper.toRecoverDTO(user, mangas, completeReadings, inProgressReadings);
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
