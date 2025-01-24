package com.reader.manga.service;

import com.reader.manga.enums.StatusType;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.model.Manga;
import com.reader.manga.model.User;
import com.reader.manga.model.UserManga;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.UserMangaRepository;
import com.reader.manga.repository.UserRepository;
import com.reader.manga.vo.UserMangaVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMangaService {

    private final MangaRepository mangaRepository;
    private final UserRepository userRepository;
    private final UserMangaRepository userMangaRepository;

    public void adicionaMangaALista(Long idManga, Long idUser) {
        try {
            Manga manga = mangaRepository.findById(idManga).orElseThrow(
                    () -> new NotFoundException("Mangá não encontrado com o id " + idManga));

            User user = userRepository.findById(idUser).orElseThrow(
                    () -> new NotFoundException("Usuário não encontrado com o id " + idUser));

            if(manga != null && user != null) {
                UserManga userManga = UserManga.builder()
                        .manga(manga)
                        .user(user)
                        .signatureDate(LocalDate.now())
                        .status(StatusType.ONGOING)
                        .nota(null)
                        .comment(null)
                        .build();
                UserManga userMangaSaved = userMangaRepository.save(userManga);

                // Adiciona o manga na lista
                user.getUserMangas().add(userMangaSaved);
                userRepository.save(user);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Argumento não encontrado, verifique as informações");
        }
    }

    public UserMangaVO getTodosMangasDoUsuario(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException("Usuário não encontrado com o id " + idUser));

        List<UserManga> mangaList = user.getUserMangas();
        List<Manga> mangaListVO = new ArrayList<>();
        
        mangaList.forEach(manga -> mangaListVO.add(manga.getManga()));
        
        UserMangaVO userManga = new UserMangaVO();
        userManga.setMangaList(mangaListVO);

        return userManga;
    }

    public Integer getQuantidadeTodosMangasDoUsuario(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException("Usuário não encontrado com o id " + idUser));

        return user.getUserMangas().size();
    }

}
