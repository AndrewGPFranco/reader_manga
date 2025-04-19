package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.animes.AnimeDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.ports.repositories.AnimeRepository;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    private final Path pastaOrigem = Paths.get("uploads/animes");

    public void uploadAnime(AnimeDTO dto) {
        try {
            if (!Objects.equals(dto.file().getContentType(), "video/mp4"))
                throw new IllegalArgumentException("Apenas vídeos MP4 são permitidos.");

            long maxSize = 500 * 1024 * 1024;
            if (dto.file().getSize() > maxSize)
                throw new IllegalArgumentException("O vídeo é muito grande.");

            String tituloSanitizado = dto.title().replaceAll("[^a-zA-Z0-9\\-_ ]", "_");
            String nomeVideo = tituloSanitizado + ".mp4";

            Path animeDir = pastaOrigem.resolve(dto.id());
            Files.createDirectories(animeDir);

            Path destino = animeDir.resolve(nomeVideo);
            Files.copy(dto.file().getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Anime anime = Anime.builder()
                    .title(dto.title()).size(dto.file().getSize())
                    .uploadDate(LocalDate.now()).build();

//            animeRepository.save(anime);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar vídeo", e);
        }
    }

    public UrlResource serveVideo(AnimeDTO dto) {
        try {
            Path file = pastaOrigem.resolve(dto.id()).resolve(dto.title() + ".mp4");
            UrlResource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable())
                return resource;
            else
                return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}