package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.chapter.UserChapterDTO;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.entities.users.UserChapter;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.UserChapterRepository;
import com.reader.manga.ports.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserChapterService {

    private final UserRepository userRepository;
    private final ChapterRepository chapterRepository;
    private final UserChapterRepository userChapterRepository;

    public void criarAssociacao(UserChapterDTO dto) {
        try {
            Optional<User> user = userRepository.findById(dto.idUser());
            Optional<Chapter> chapter = chapterRepository.findById(dto.idChapter());

            if(user.isPresent() && chapter.isPresent()) {
                UserChapter userChapter = UserChapter.builder()
                        .user_id(user.get())
                        .chapter_id(chapter.get())
                        .progress(dto.progress())
                        .build();

                userChapterRepository.save(userChapter);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
