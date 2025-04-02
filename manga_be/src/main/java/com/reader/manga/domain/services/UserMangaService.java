package com.reader.manga.domain.services;

import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.entities.users.FavoriteMangaUser;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.entities.users.UserManga;
import com.reader.manga.domain.interfaces.iListaMangasPorUsuario;
import com.reader.manga.ports.repositories.FavoriteMangaRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.ports.repositories.UserMangaRepository;
import com.reader.manga.ports.repositories.UserRepository;
import com.reader.manga.domain.valueobjects.users.MangaUserVO;
import com.reader.manga.domain.valueobjects.users.UserMangaVO;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMangaService {

    private final MangaRepository jpaMangaRepository;
    private final UserRepository jpaUserRepository;
    private final UserMangaRepository jpaUserMangaRepository;
    private final FavoriteMangaRepository jpaFavoriteMangaRepository;

    private static final String USER_NOT_FOUND = "Usuário nao encontrado com o id: ";
    private static final String MANGA_NOT_FOUND = "Manga nao encontrado com o id: ";

    public void adicionaMangaALista(Long idManga, Long idUser) {
        try {
            Manga manga = getManga(idManga);
            User user = getUser(idUser);

            if(manga != null && user != null) {
                UserManga userManga = UserManga.builder()
                        .manga(manga)
                        .user(user)
                        .signatureDate(LocalDate.now())
                        .status(StatusType.ONGOING)
                        .nota(null)
                        .comment(null)
                        .build();
                UserManga userMangaSaved = jpaUserMangaRepository.save(userManga);

                // Adiciona o manga na lista
                user.getUserMangas().add(userMangaSaved);
                jpaUserRepository.save(user);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Argumento não encontrado, verifique as informações");
        }
    }

    private User getUser(Long idUser) {
        return jpaUserRepository.findById(idUser).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND + idUser));
    }

    private Manga getManga(Long idManga) {
        return jpaMangaRepository.findById(idManga).orElseThrow(
                () -> new NotFoundException(MANGA_NOT_FOUND + idManga));
    }

    public void removeMangaDaLista(Long idManga, Long idUser) {
        try {
            Manga manga = getManga(idManga);
            User user = getUser(idUser);

            if(manga != null && user != null) {
                jpaUserMangaRepository.deletaAssociacao(idUser, idManga);
                jpaFavoriteMangaRepository.removerMangaDaListaDeFavoritos(idManga, idUser);
                // Adiciona o manga na lista
                user.getUserMangas().removeIf(m -> m.getManga().getId().equals(idManga));
                user.getMangaFavorites().removeIf(m -> m.getManga().getId().equals(idManga));
                jpaUserRepository.save(user);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Manga ou Usuário inexistente.");
        }
    }

    public UserMangaVO getTodosMangasDoUsuario(Long idUser) {
        User user = getUser(idUser);

        List<UserManga> mangaList = user.getUserMangas();

        return getMangasDoUsuario(mangaList, idUser);
    }

    public UserMangaVO getMangasFavoritosDoUsuario(Long idUser) {
        User user = getUser(idUser);

        List<FavoriteMangaUser> mangaList = user.getMangaFavorites();

        return getMangasDoUsuario(mangaList, idUser);
    }

    private @NotNull UserMangaVO getMangasDoUsuario(List<? extends iListaMangasPorUsuario> mangaList, Long idUser) {
        List<MangaUserVO> mangaListVO = new ArrayList<>();

        mangaList.forEach(m -> {
            Manga manga = m.getManga();
            FavoriteMangaUser favoriteMangaUser = jpaFavoriteMangaRepository.favoriteIsTrue(manga.getId(), idUser);
            MangaUserVO mangaUserVO = MangaUserVO.builder().id(manga.getId()).title(manga.getTitle()).image(manga.getImage())
                    .author(manga.getAuthor()).favorite(favoriteMangaUser != null).gender(manga.getGender())
                    .status(manga.getStatus()).size(manga.getSize()).build();
            mangaListVO.add(mangaUserVO);
        });

        UserMangaVO userManga = new UserMangaVO();
        userManga.setMangaList(mangaListVO);
        return userManga;
    }

    public Integer getQuantidadeTodosMangasDoUsuario(Long idUser) {
        User user = getUser(idUser);

        return user.getUserMangas().size();
    }

    public void adicionaMangaAListaDeFavoritos(Long idManga, Long idUser) {
        try {
            Manga manga = getManga(idManga);
            User user = getUser(idUser);

            if(manga != null && user != null) {
                FavoriteMangaUser favoritarManga = FavoriteMangaUser.builder()
                        .manga(manga)
                        .user(user)
                        .build();

                FavoriteMangaUser favoriteManga = jpaFavoriteMangaRepository.save(favoritarManga);

                // Adiciona o manga na lista
                user.getMangaFavorites().add(favoriteManga);
                jpaUserRepository.save(user);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Argumento não encontrado, verifique as informações");
        }
    }

    public void changeMangaFavoriteStatus(Long idManga, Long idUser) {
        Manga manga = jpaMangaRepository.findById(idManga).orElseThrow(() -> new NotFoundException("Mangá not found."));
        User user = jpaUserRepository.findById(idUser).orElseThrow(() -> new NotFoundException("User not found."));

        if(user != null && manga != null) {
            FavoriteMangaUser isFavorite = jpaFavoriteMangaRepository.favoriteIsTrue(idManga, idUser);

            if(isFavorite == null) {
                FavoriteMangaUser favoriteManga = FavoriteMangaUser
                        .builder().manga(manga).user(user).build();

                jpaFavoriteMangaRepository.save(favoriteManga);

                user.getMangaFavorites().add(favoriteManga);
                jpaUserRepository.save(user);
            } else {
                jpaFavoriteMangaRepository.removerMangaDaListaDeFavoritos(idManga, idUser);
                user.getMangaFavorites().removeIf(favoriteManga -> favoriteManga.getManga().getId().equals(idManga));
                jpaUserRepository.save(user);
            }
        }
    }

    /**
     * TODO: ajustar
     * @param idUser
     * @param idManga
     */
    public void deleteAssociacaoUserMangaFavorite(Long idUser, Long idManga) {
        jpaFavoriteMangaRepository.removerMangaDaListaDeFavoritos(idManga, idUser);
    }

    /**
     * TODO: ajustar
     * @param idUser
     * @param idManga
     */
    public void deleteAssociacaoUserManga(Long idUser, Long idManga) {
        jpaUserMangaRepository.deletaAssociacao(idUser, idManga);
    }

    public User getUserById(Long idUser) {
        return jpaUserRepository.findById(idUser).orElseThrow(() ->
                new NotFoundException("Nenhum usuário encontrado com o id: " + idUser));
    }

}
