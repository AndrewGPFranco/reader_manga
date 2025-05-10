package com.reader.manga.domain.facades;

import com.reader.manga.adapters.input.mappers.CommentMapper;
import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;
import com.reader.manga.domain.enums.FeedbackEpisodeType;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeCommentsVO;
import com.reader.manga.ports.repositories.FavoriteEpisodeUserRepository;
import com.reader.manga.ports.repositories.VideosCommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimesManagementFacade {

    private final CommentMapper commentMapper;
    private final VideosCommentsRepository videosCommentsRepository;
    private final FavoriteEpisodeUserRepository favoriteEpisodeUserRepository;

    public FeedbackEpisodeType feedbackOfVideoByUser(Long idUser, Long idEpisode) {
        String feedbackByUserAndEpisode = favoriteEpisodeUserRepository.getFeedbackByUserAndEpisode(idUser, idEpisode);
        if(feedbackByUserAndEpisode == null)
            return FeedbackEpisodeType.NOTHING;

        return FeedbackEpisodeType.valueOf(feedbackByUserAndEpisode);
    }

    public List<EpisodeCommentsVO> getVideoComments(Long idEpisode) {
        List<VideosComments> commentsByEpisode = videosCommentsRepository
                .getCommentsByEpisode(idEpisode);

        return commentsByEpisode.stream().map(commentMapper::entityEpisodeCommentToVO).toList();
    }

    public void addCommentToEpisode(VideosComments comment) {
        videosCommentsRepository.save(comment);
    }

    public void saveHandleFeedback(FavoriteEpisodeUser favoriteEpisodeUser) {
        favoriteEpisodeUserRepository.save(favoriteEpisodeUser);
    }

    public FavoriteEpisodeUser favoriteEpisodeByUser(Long idUser, Long idEpisode) {
        return favoriteEpisodeUserRepository.favoriteEpisodeByUser(idUser, idEpisode);
    }

    public void removeFavorite(Long idUser, Long idEpisode) {
        FavoriteEpisodeUser alreadyExists = favoriteEpisodeByUser(idUser, idEpisode);
        favoriteEpisodeUserRepository.deleteById(alreadyExists.getId());
    }
}
