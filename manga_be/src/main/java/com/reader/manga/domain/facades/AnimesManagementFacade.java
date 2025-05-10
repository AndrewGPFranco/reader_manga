package com.reader.manga.domain.facades;

import com.reader.manga.adapters.input.mappers.CommentMapper;
import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.domain.enums.FeedbackEpisodeType;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeCommentsVO;
import com.reader.manga.ports.repositories.FavoriteEpisodeUserRepository;
import com.reader.manga.ports.repositories.VideosCommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Pageable getPageable(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }

    public void addCommentToEpisode(VideosComments comment) {
        videosCommentsRepository.save(comment);
    }

}
