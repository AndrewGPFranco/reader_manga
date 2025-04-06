package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.chapter.UserChapterDTO;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.entities.users.UserChapter;
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

    private final UserMangaService userService;
    private final UserRepository userRepository;
    private final ChapterRepository chapterRepository;
    private final UserChapterRepository userChapterRepository;

    public void criarAssociacao(UserChapterDTO dto) {
        try {
            Optional<User> user = userRepository.findById(dto.idUser());
            Optional<Chapter> chapter = chapterRepository.findById(dto.idChapter());

            UserChapter valorJaExistente = userChapterRepository.findByIdChapterAndUser(dto.idChapter(), dto.idUser());

            if(user.isPresent() && chapter.isPresent()) {
                if(valorJaExistente != null) {
                    valorJaExistente.setProgress(
                            dto.progress() > valorJaExistente.getProgress() ?
                                    dto.progress() : valorJaExistente.getProgress()
                    );
                    valorJaExistente.setStatus(
                            chapter.get().getNumberPages().equals(dto.progress()) ?
                                    StatusType.FINISHED : StatusType.ONGOING
                    );
                    userChapterRepository.save(valorJaExistente);
                } else {
                    UserChapter userChapter = UserChapter.builder()
                            .user_id(user.get())
                            .chapter_id(chapter.get())
                            .progress(dto.progress())
                            .status(
                                    chapter.get().getNumberPages().equals(dto.progress()) ?
                                            StatusType.FINISHED : StatusType.ONGOING
                            )
                            .build();
                    userChapterRepository.save(userChapter);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<Long, Integer> obtemProgressoLeituraUsuario(Long idUser, Manga mangaRecuperado) {
        User user = userService.getUserById(idUser);
        Map<Long, Integer> progressos = new HashMap<>();

        adicionaCapituloAoMapaProgresso(mangaRecuperado, user, progressos);
        return progressos;
    }

    private void adicionaCapituloAoMapaProgresso(Manga manga, User user, Map<Long, Integer> progressos) {
        for (Chapter chapter : manga.getChapters()) {
            UserChapter chapterId = userChapterRepository.findByIdChapterAndUser(chapter.getId(), user.getId());
            Integer progressoAtual = chapterId != null ? chapterId.getProgress() : 0;
            progressos.put(chapter.getId(), progressoAtual);
        }
    }

    public void atualizaProgresso(List<Chapter> chapters, Map<Long, Integer> mapaProgressos) {
        chapters.forEach(chapter -> {
            Integer progresso = mapaProgressos.get(chapter.getId());
            if(progresso != null) {
                chapter.setReadingProgress(progresso);
                chapter.setStatus(
                        mapaProgressos.get(chapter.getId()).equals(chapter.getNumberPages()) ?
                        StatusType.FINISHED : StatusType.ONGOING
                );
            }
        });
    }

    public void deleteAssociacao(Long idChapter, Long idUser) {
        try {
            userChapterRepository.deleteAssociacao(idChapter, idUser);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar associação.");
        }
    }
}
