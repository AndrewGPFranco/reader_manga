package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.manga.GetMangaDTO;
import com.reader.manga.adapters.input.dtos.manga.MangaDTO;
import com.reader.manga.adapters.input.dtos.manga.UpdateMangaDTO;
import com.reader.manga.adapters.input.dtos.utils.UserData;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.UserChapterService;
import com.reader.manga.domain.valueobjects.mangas.AvaliacaoMangaVO;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.domain.services.MangaService;
import com.reader.manga.domain.services.UserMangaService;
import com.reader.manga.domain.valueobjects.mangas.CoversMangaVO;
import com.reader.manga.domain.valueobjects.mangas.InfoMangaVO;
import com.reader.manga.domain.valueobjects.users.MangaCoverVO;
import com.reader.manga.domain.valueobjects.users.MangaUserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/manga")
@RequiredArgsConstructor
public class MangaController {

    private final MangaService service;
    private final WebClient webClient;
    private final MangaRepository repository;
    private final UserMangaService userMangaService;
    private final UserChapterService userChapterService;
    private static final Logger logger = LoggerFactory.getLogger(MangaController.class);

    @GetMapping("/read/{id}")
    public ResponseEntity<Manga> getMangaById(@PathVariable Long id) {
        logger.info("*******************Reading manga!*******************");
        Manga manga = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(manga);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createManga(@RequestBody @Valid MangaDTO dto) {
        logger.info("*******************Creating mangá!*******************");
        GetMangaDTO manga = service.createManga(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(manga);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteManga(@RequestBody UserData userDto) {
        logger.info("*******************Deleting mangá!*******************");
        service.deleteManga(userDto);
        return ResponseEntity.status(HttpStatus.OK).body("Mangá deleted successfully!");
    }

    @GetMapping("/readAll/{idUser}")
    public ResponseEntity<Set<MangaUserVO>> readAllMangas(@PathVariable Long idUser) {
        logger.info("*******************Reading all mangas!*******************");
        Set<MangaUserVO> mangas = service.readAllMangas(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(mangas);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateMangaById(@PathVariable Long id, @RequestBody UpdateMangaDTO dto) {
        service.updateManga(id, dto);
        logger.info("*******************Updating mangá!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Mangá updated successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllMangaForSelect() {
        List<Manga> allMangas = service.getAll();
        return ResponseEntity.ok().body(allMangas);
    }

    @GetMapping("/chapter_id/by-id/{id}")
    public ResponseEntity<List<Chapter>> getChapterByManga(@PathVariable Long id) {
        List<Chapter> chapters = service.getChaptersByManga(id);
        return ResponseEntity.ok().body(chapters);
    }

    @GetMapping("/get-covers")
    public Mono<List<MangaCoverVO>> getMangaCovers() {
        List<String> titles = List.of("Naruto", "Death Note", "One Piece", "Fullmetal Alchemist", "Dragon Ball");

        return Flux.fromIterable(titles)
                .flatMap(t -> service.fetchCoverForTitle(t, webClient))
                .take(5)
                .collectList();
    }

    @GetMapping("/my-covers/{max}")
    public ResponseEntity<List<CoversMangaVO>> getMyCovers(@PathVariable int max) {
        List<Map.Entry<String, CoversMangaVO>> randomCovers = service.getRandomCovers(max);
        List<CoversMangaVO> finalList = new ArrayList<>();
        randomCovers.forEach(r -> finalList.add(r.getValue()));
        return ResponseEntity.ok().body(finalList);
    }

    @GetMapping("/get-info-manga/{titulo}/{idUser}")
    public ResponseEntity<InfoMangaVO> getInfoManga(@PathVariable String titulo, @PathVariable Long idUser) {
        Manga manga = service.findByTitle(titulo);
        Map<Long, Integer> mapaProgressos = userChapterService.obtemProgressoLeituraUsuario(idUser, manga);

        userChapterService.atualizaProgresso(manga.getChapters(), mapaProgressos);
        boolean isInLibrary = userMangaService.mangaIsInUserLibrary(manga.getId(), idUser);

        return ResponseEntity.ok().body(InfoMangaVO.builder()
                        .id(manga.getId())
                        .nota(userMangaService.getNotaDoMangaPeloUsuario(manga.getId(), idUser))
                        .title(manga.getTitle())
                        .size(manga.getSize())
                        .creationDate(manga.getCreationDate())
                        .description(manga.getDescription())
                        .chapters(manga.getChapters())
                        .gender(manga.getGender())
                        .endDate(manga.getClosingDate())
                        .author(manga.getAuthor())
                        .status(manga.getStatus())
                        .image(manga.getImage())
                        .isInUserLibrary(isInLibrary)
                .build());
    }

    @GetMapping("/get-pageable")
    public Page<Manga> getMangasPaginados(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long idUser
    ) {
        int numeroDaPagina = --pageNumber;
        Pageable pageable = PageRequest.of(numeroDaPagina, size);
        Page<Manga> mangasPaginados = repository.findAll(pageable);
        List<MangaUserVO> todosMangasDoUsuario = userMangaService.getTodosMangasDoUsuario(idUser).getMangaList();

        mangasPaginados.forEach(mp -> {
            List<MangaUserVO> list = todosMangasDoUsuario.stream().filter(tm ->
                    tm.title().equals(mp.getTitle())).toList();

            if(!list.isEmpty())
                mp.setFavorite(true);
        });

        return mangasPaginados;
    }

    @GetMapping("/nome-mangas")
    public ResponseEntity<List<String>> getNomeDosMangas() {
        List<String> apenasNomeDosMangas = service.getApenasNomeDosMangas();

        return ResponseEntity.ok().body(apenasNomeDosMangas);
    }

    @GetMapping
    public Page<Manga> getMangaPesquisado(@RequestParam String pesquisado,
                                          @RequestParam(defaultValue = "0") int pageNumber) {
        return service.getMangaPesquisado(pesquisado, pageNumber);
    }

    @PostMapping("/avaliacao")
    public void avaliaManga(@RequestBody AvaliacaoMangaVO vo, @AuthenticationPrincipal User user) {
        userMangaService.avaliaManga(vo, user);
    }

}
