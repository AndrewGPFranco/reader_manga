package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.chapter.UserChapterDTO;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.entities.users.UserChapter;
import com.reader.manga.domain.entities.users.UserManga;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.UserChapterRepository;
import com.reader.manga.ports.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserChapterService {

    private final MangaService mangaService;
    private final UserMangaService userService;
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

    public Map<Long, Integer> obtemProgressoLeituraUsuario(Long idUser) {
        User user = userService.getUserById(idUser);
        Map<Long, Integer> progressos = new HashMap<>();

        // Mangás do usuário
        List<UserManga> userMangas = user.getUserMangas();
        for (UserManga mangas : userMangas) {
            Manga manga = mangas.getManga();
            for (Chapter chapter : manga.getChapters()) {
                UserChapter chapterId = userChapterRepository.findByIdChapterAndUser(chapter.getId(), user.getId());
                Integer progressoAtual = chapterId != null ? chapterId.getProgress() : 0;
                progressos.put(chapter.getId(), progressoAtual);
            }
        }

        return progressos;
    }

    public void atualizaProgresso(List<Chapter> chapters, Map<Long, Integer> mapaProgressos) {
        chapters.forEach(chapter -> {
            chapter.setReadingProgress(mapaProgressos.get(chapter.getId()));
            chapter.setStatus(
                    mapaProgressos.get(chapter.getId()).equals(chapter.getNumberPages()) ?
                    StatusType.FINISHED : StatusType.ONGOING
            );
        });
    }
}
