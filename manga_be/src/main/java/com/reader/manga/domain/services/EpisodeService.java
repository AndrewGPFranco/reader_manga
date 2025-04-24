package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.entities.animes.Episode;
import com.reader.manga.domain.exceptions.NotFoundException;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final AnimeService animeService;
    private final EpisodeRepository episodeRepository;

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

    public List<Episode> obterEpisodiosDoAnime(Long idAnime) {
        return episodeRepository.findByIdAndAnime(idAnime);
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
}
