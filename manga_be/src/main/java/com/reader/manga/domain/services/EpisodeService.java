package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.episode.CommentDTO;
import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
import com.reader.manga.adapters.input.dtos.episode.FeedbackDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.entities.animes.Episode;
import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.FeedbackEpisodeType;
import com.reader.manga.domain.enums.TagType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.facades.AnimesManagementFacade;
import com.reader.manga.domain.facades.UserManagementFacade;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeCommentsVO;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeDisplayVO;
import com.reader.manga.domain.valueobjects.screens.listing.animes.AnimeListingVO;
import com.reader.manga.domain.valueobjects.screens.listing.animes.EpisodeToAnimesVO;
import com.reader.manga.ports.repositories.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final AnimeService animeService;
    private final AnimeUserService animeUserService;
    private final EpisodeRepository episodeRepository;
    private final AnimesManagementFacade animesManagementFacade;
    private final UserManagementFacade userManagementFacade;

    private final Path pastaOrigem = Paths.get("uploads/animes");

    public void uploadAnime(EpisodeDTO dto) {
        try {
            if (!Objects.equals(dto.file().getContentType(), "video/mp4"))
                throw new IllegalArgumentException("Apenas vídeos MP4 são permitidos.");

            Anime anime = animeService.getAnimeById(dto.animeId());

            long maxSize = 500 * 1024 * 1024;

            if (dto.file().getSize() > maxSize)
                throw new IllegalArgumentException("O vídeo é muito grande.");

            String nomeVideo = "episode_" + dto.id() + "_.mp4";

            Path animeDir = pastaOrigem.resolve(anime.getTitle());
            Files.createDirectories(animeDir);

            Path destino = animeDir.resolve(nomeVideo);
            Files.copy(dto.file().getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Episode episode = Episode.builder()
                    .title(dto.title())
                    .uri(destino.toAbsolutePath().toString())
                    .numberEpisode(Integer.valueOf(dto.id()))
                    .views(0)
                    .uploaded(LocalDate.now())
                    .commentsList(new ArrayList<>())
                    .anime(anime).build();

            episodeRepository.save(episode);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar vídeo", e);
        }
    }

    public UrlResource serveVideo(EpisodeDTO dto) {
        try {
            Episode episode = getEpisodeById(dto.animeId());

            UrlResource resource = new UrlResource("file:" + episode.getUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public AnimeListingVO obterEpisodiosDoAnime(Long idAnime, Long idUser) {
        Anime anime = animeService.getAnimeById(idAnime);
        List<Episode> listaEpisodios = episodeRepository.findByIdAndAnime(idAnime);
        List<EpisodeToAnimesVO> episodes = new ArrayList<>();

        listaEpisodios.forEach(episode -> episodes.add(EpisodeToAnimesVO.builder()
                .id(episode.getId()).titleEpisode(episode.getTitle()).uriEpisode(episode.getUri())
                .numberEpisode(episode.getNumberEpisode()).build()));

        Integer nota = animeUserService.getNotaPeloUsuario(idUser, idAnime);
        boolean isFavorite = animeService.animeIsFavorite(idUser, idAnime);

        return AnimeListingVO.builder()
                .idAnime(anime.getId())
                .titleAnime(anime.getTitle())
                .episodes(episodes)
                .note(nota)
                .tags(List.of(TagType.HD, TagType.ANIME))
                .isFavorite(isFavorite)
                .launchYear(anime.getReleaseDate())
                .uriImage(anime.getUriImage())
                .build();
    }

    public String getVideoById(Long id) {
        Episode episode = getEpisodeById(id);
        String uri = episode.getUri();
        String[] uriTratada = uri.split("/app");
        return uriTratada[1];
    }

    private Episode getEpisodeById(Long id) {
        return episodeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Nenhum episódio encontrado com o ID: " + id));
    }

    public EpisodeDisplayVO getEpisodeInfos(Long idEpisode, User user) {
        String uri = getVideoById(idEpisode);
        Episode episode = getEpisodeById(idEpisode);
        String uriProfilePhoto = userManagementFacade.getUriProfilePhoto(user.getId());
        FeedbackEpisodeType feedback = animesManagementFacade.feedbackOfVideoByUser(user.getId(), idEpisode);
        List<EpisodeCommentsVO> videoComments = animesManagementFacade.getVideoComments(idEpisode);

        return EpisodeDisplayVO.builder()
                .uriEpisode(uri)
                .titleEpisode(episode.getTitle())
                .amountViews(episode.getViews())
                .uploaded(episode.getUploaded())
                .feedback(feedback)
                .commentsList(videoComments)
                .uriPath(uriProfilePhoto)
                .build();
    }

    public void updateAmountViews(Long idEpisode) {
        Episode episode = getEpisodeById(idEpisode);
        int newAmount = episode.getViews() + 1;
        episode.setViews(newAmount);
        episodeRepository.save(episode);
    }

    public void addCommentToEpisode(CommentDTO dto, User user) {
        Episode episode = getEpisodeById(dto.idEpisode());
        VideosComments comment = VideosComments.builder()
                .user(user)
                .episode(episode)
                .comment(dto.comment())
                .build();

        animesManagementFacade.addCommentToEpisode(comment);
    }

    public void handleFeedback(FeedbackDTO dto, User user) {
        FeedbackEpisodeType feedbackType = getFeedbackTypeByDTO(dto.feedback());
        Episode episode = getEpisodeById(dto.idEpisode());
        FeedbackEpisodeType feedbackAlreadyExists = animesManagementFacade.feedbackOfVideoByUser(user.getId(), dto.idEpisode());

        if (feedbackAlreadyExists.equals(feedbackType)) {
            animesManagementFacade.removeFavorite(user.getId(), dto.idEpisode());
        } else if (feedbackAlreadyExists.equals(FeedbackEpisodeType.NOTHING)) {
            animesManagementFacade.saveHandleFeedback(FavoriteEpisodeUser.builder()
                    .user(user)
                    .episode(episode)
                    .feedback(feedbackType)
                    .build());
        } else {
            FavoriteEpisodeUser valueDatabase = animesManagementFacade.favoriteEpisodeByUser(user.getId(), dto.idEpisode());
            valueDatabase.setFeedback(feedbackType);
            animesManagementFacade.saveHandleFeedback(valueDatabase);
        }
    }

    private FeedbackEpisodeType getFeedbackTypeByDTO(String feedbackStr) {
        return switch (feedbackStr) {
            case "Gostei" -> FeedbackEpisodeType.LIKE;
            case "Não gostei" -> FeedbackEpisodeType.DISLIKE;
            case "Sem feedback" -> FeedbackEpisodeType.NOTHING;
            default -> throw new IllegalArgumentException(String.format("Nenhum tipo encontrado para %s", feedbackStr));
        };
    }
}
