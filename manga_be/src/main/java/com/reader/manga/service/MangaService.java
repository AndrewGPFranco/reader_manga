package com.reader.manga.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.dto.manga.GetMangaDTO;
import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.mapper.MangaMapper;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.vo.MangaCoverVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class MangaService {

    private final MangaRepository repository;

    private final MangaMapper mangaMapper;

    public MangaService(MangaRepository repository, MangaMapper mangaMapper) {
        this.repository = repository;
        this.mangaMapper = mangaMapper;
    }

    public GetMangaDTO createManga(MangaDTO dto) {
        try {
            Manga manga = new Manga(dto.title(), dto.description(), dto.size(), dto.creationDate(), dto.closingDate(), dto.status(), dto.gender(), dto.author(),  dto.image());
            Manga savedManga = repository.save(manga);
            return new GetMangaDTO(savedManga.getId(), savedManga.getTitle(), savedManga.getDescription(), savedManga.getSize(), savedManga.getCreationDate(), savedManga.getClosingDate(), savedManga.getStatus(), savedManga.getGender(), savedManga.getAuthor(),  savedManga.getImage());
        }catch (Exception e) {
            throw new CreationErrorException("Error creating Manga. Please try again... " + e.getMessage());
        }
    }

    public void deleteManga(Long id) {
        Optional<Manga> mangaById = repository.findById(id);
        if(mangaById.isEmpty()){
            throw new NotFoundException("No manga found with the id: " + id + ".");
        }
        repository.deleteById(id);
    }

    public List<Manga> readAllMangas(Pageable pageable) {
        if (pageable == null) {
            return repository.findAll();
        }

        Page<Manga> pageResult = repository.findAll(pageable);
        return pageResult.getContent();
    }

    public void updateManga(Long id, UpdateMangaDTO dto) {
        Manga manga = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manga not found"));

        UtilsService.updateField(dto.title(), manga::setTitle);
        UtilsService.updateField(dto.description(), manga::setDescription);
        UtilsService.updateField(dto.size(), manga::setSize);
        UtilsService.updateField(dto.creationDate(), manga::setCreationDate);
        UtilsService.updateField(dto.closingDate(), manga::setClosingDate);
        UtilsService.updateField(dto.status(), manga::setStatus);
        UtilsService.updateField(dto.author(), manga::setAuthor);
        UtilsService.updateField(dto.gender(), manga::setGender);
        UtilsService.updateField(dto.image(), manga::setImage);

        repository.save(manga);
    }

    public List<Manga> getAll() {
        return repository.findAll();
    }

    public Manga findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Mangá not found"));
    }

    public List<Chapter> getChaptersByManga(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Mangá not found")).getChapters();
    }

    public Mono<MangaCoverVO> fetchCoverForTitle(String title, WebClient webClient) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/manga")
                        .queryParam("title", title)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode manga = data.get(0);
                        String mangaId = manga.get("id").asText();
                        return fetchCoverImage(mangaId, webClient);
                    }
                    return Mono.empty();
                });
    }

    private Mono<MangaCoverVO> fetchCoverImage(String mangaId, WebClient webClient) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cover")
                        .queryParam("manga[]", mangaId)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode cover = data.get(0);
                        String fileName = cover.get("attributes").get("fileName").asText();
                        String imageUrl = "https://uploads.mangadex.org/covers/" + mangaId + "/" + fileName;

                        return MangaCoverVO.builder().id(mangaId).imageUrl(imageUrl).build();
                    }
                    return null;
                });
    }

    public void changeMangaFavoriteStatus(boolean isFavorite, Long idManga) {
        Manga manga = repository.findById(idManga).orElseThrow(() -> new NotFoundException("Mangá not found."));
        manga.setFavorite(isFavorite);
        updateManga(idManga, mangaMapper.mangaToUpdateMangaDTO(manga));
    }
}