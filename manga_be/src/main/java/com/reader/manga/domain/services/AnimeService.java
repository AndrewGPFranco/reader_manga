package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.anime.AnimeDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.ports.repositories.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> getAllAnimes() {
        return animeRepository.findAll();
    }

    public void registraAnime(AnimeDTO dto) {
        try {
            animeRepository.save(
                    Anime.builder()
                            .title(dto.title())
                            .uploadDate(LocalDate.now())
                            .uriImage(dto.uriImage())
                            .episodes(new ArrayList<>())
                            .build());
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Anime já cadastrado!");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao registrar o anime!");
        }
    }
}