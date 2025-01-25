package com.reader.manga.service;

import com.reader.manga.enums.StatusType;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.model.FavoriteMangaUser;
import com.reader.manga.model.Manga;
import com.reader.manga.model.User;
import com.reader.manga.model.UserManga;
import com.reader.manga.repository.FavoriteMangaRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.UserMangaRepository;
import com.reader.manga.repository.UserRepository;
import com.reader.manga.interfaces.ListaMangasPorUsuarioInterface;
import com.reader.manga.vo.MangaUserVO;
import com.reader.manga.vo.UserMangaVO;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    private final FavoriteMangaRepository favoriteMangaRepository;

    private static final String USER_NOT_FOUND = "Usuário nao encontrado com o id: ";
    private static final String MANGA_NOT_FOUND = "Manga nao encontrado com o id: ";

    public void adicionaMangaALista(Long idManga, Long idUser) {
        try {
            Manga manga = mangaRepository.findById(idManga).orElseThrow(
                    () -> new NotFoundException(MANGA_NOT_FOUND + idManga));

            User user = userRepository.findById(idUser).orElseThrow(
                    () -> new NotFoundException(USER_NOT_FOUND + idUser));

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
                () -> new NotFoundException(USER_NOT_FOUND + idUser));

        List<UserManga> mangaList = user.getUserMangas();

        return getMangasDoUsuario(mangaList, idUser);
    }

    public UserMangaVO getMangasFavoritosDoUsuario(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND + idUser));

        List<FavoriteMangaUser> mangaList = user.getMangaFavorites();

        return getMangasDoUsuario(mangaList, idUser);
    }

    private @NotNull UserMangaVO getMangasDoUsuario(List<? extends ListaMangasPorUsuarioInterface> mangaList, Long idUser) {
        List<MangaUserVO> mangaListVO = new ArrayList<>();

        mangaList.forEach(m -> {
            Manga manga = m.getManga();
            FavoriteMangaUser favoriteMangaUser = favoriteMangaRepository.favoriteIsTrue(manga.getId(), idUser);
            MangaUserVO mangaUserVO = MangaUserVO.builder().id(manga.getId()).title(manga.getTitle()).image(manga.getImage())
                    .author(manga.getAuthor()).favorite(favoriteMangaUser != null).gender(manga.getGender()).size(manga.getSize()).build();
            mangaListVO.add(mangaUserVO);
        });

        UserMangaVO userManga = new UserMangaVO();
        userManga.setMangaList(mangaListVO);
        return userManga;
    }

    public Integer getQuantidadeTodosMangasDoUsuario(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND + idUser));

        return user.getUserMangas().size();
    }

    public void adicionaMangaAListaDeFavoritos(Long idManga, Long idUser) {
        try {
            Manga manga = mangaRepository.findById(idManga).orElseThrow(
                    () -> new NotFoundException(MANGA_NOT_FOUND + idManga));

            User user = userRepository.findById(idUser).orElseThrow(
                    () -> new NotFoundException(USER_NOT_FOUND + idUser));

            if(manga != null && user != null) {
                FavoriteMangaUser favoritarManga = FavoriteMangaUser.builder()
                        .manga(manga)
                        .user(user)
                        .build();

                FavoriteMangaUser favoriteManga = favoriteMangaRepository.save(favoritarManga);

                // Adiciona o manga na lista
                user.getMangaFavorites().add(favoriteManga);
                userRepository.save(user);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Argumento não encontrado, verifique as informações");
        }
    }

    public void changeMangaFavoriteStatus(Long idManga, Long idUser) {
        Manga manga = mangaRepository.findById(idManga).orElseThrow(() -> new NotFoundException("Mangá not found."));
        User user = userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("User not found."));

        if(user != null && manga != null) {
            FavoriteMangaUser isFavorite = favoriteMangaRepository.favoriteIsTrue(idManga, idUser);

            if(isFavorite == null) {
                FavoriteMangaUser favoriteManga = FavoriteMangaUser
                        .builder().manga(manga).user(user).build();

                favoriteMangaRepository.save(favoriteManga);

                user.getMangaFavorites().add(favoriteManga);
                userRepository.save(user);
            } else {
                favoriteMangaRepository.removerMangaDaListaDeFavoritos(idManga, idUser);
                user.getMangaFavorites().removeIf(favoriteManga -> favoriteManga.getManga().getId().equals(idManga));
                userRepository.save(user);
            }
        }
    }

}
