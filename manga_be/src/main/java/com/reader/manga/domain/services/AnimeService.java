package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.anime.AnimeDTO;
import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.ports.repositories.AnimeRepository;
import jakarta.validation.ConstraintDeclarationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    private final Path pastaOrigem = Paths.get("uploads/animes");

    public void uploadAnime(EpisodeDTO dto) {
        try {
            if (!Objects.equals(dto.file().getContentType(), "video/mp4"))
                throw new IllegalArgumentException("Apenas vídeos MP4 são permitidos.");

            long maxSize = 500 * 1024 * 1024;
            if (dto.file().getSize() > maxSize)
                throw new IllegalArgumentException("O vídeo é muito grande.");

            String nomeVideo = "episode_" + dto.id() + "_.mp4";

            Path animeDir = pastaOrigem.resolve(dto.id());
            Files.createDirectories(animeDir);

            Path destino = animeDir.resolve(nomeVideo);
            Files.copy(dto.file().getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Anime anime = Anime.builder()
                    .title(dto.title())
                    .uploadDate(LocalDate.now()).build();

            animeRepository.save(anime);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar vídeo", e);
        }
    }

    public UrlResource serveVideo(EpisodeDTO dto) {
        try {
            Path file = pastaOrigem.resolve(dto.id()).resolve("episode_" + dto.id() + "_.mp4");
            UrlResource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable())
                return resource;
            else
                return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public void registraAnime(AnimeDTO dto) {
        try {
            animeRepository.save(
                    Anime.builder()
                            .title(dto.title())
                            .uploadDate(LocalDate.now())
                            .episodes(new ArrayList<>())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Anime já cadastrado!");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao registrar o anime!");
        }
    }
}