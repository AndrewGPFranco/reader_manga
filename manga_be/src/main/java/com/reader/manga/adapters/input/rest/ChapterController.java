package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.chapter.*;
import com.reader.manga.adapters.input.dtos.page.PageDTO;
import com.reader.manga.adapters.input.dtos.page.UpdatePageDTO;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Pagina;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.services.ChapterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    private final ChapterService service;

    public ChapterController(ChapterService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createChapter(@RequestBody ChapterDTO dto) {
        logger.info("*******************Creating mangá!*******************");
        service.createChapter(dto);
        GetChapterDTO chapterDTO = new GetChapterDTO(dto.title(), 0,
                StatusType.ONGOING, 0);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/readAll")
    public Page<Chapter> readAllChapters(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("*******************Reading all chapters!*******************");
        Pageable pageable = PageRequest.of(pageNumber, size);
        return service.readAllChapters(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteChapter(@PathVariable Long id) {
        logger.info("*******************Deleting mangá!*******************");
        service.deleteChapter(id);
        return ResponseEntity.status(HttpStatus.OK).body("Chapter deleted successfully!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateChapterById(@PathVariable Long id, @RequestBody UpdateChapterDTO dto) {
        service.updateChapter(id, dto);
        logger.info("*******************Updating chapter!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Chapter updated successfully!");
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Object> getChapterById(@PathVariable Long id) {
        Chapter chapter = service.getChapterByID(id);
        logger.info("*******************Searching chapter!*******************");
        List<Pagina> pages = chapter.getPages();
        pages.sort(Comparator.comparing(Pagina::getId));
        return ResponseEntity.ok().body(pages);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register/page")
    public ResponseEntity<String> registerPageChapter(@RequestBody @Valid PageDTO pageDTO) {
        service.pageChapterRegister(pageDTO);
        logger.info("Chapter page registered successfully");
        return ResponseEntity.ok().body("Chapter page registered successfully");
    }

    @GetMapping("/getAll-pages")
    public Page<Pagina> getAllPages(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return service.getAllPages(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/page/{idPage}")
    public ResponseEntity<String> deletePage(@PathVariable Long idPage) {
        logger.info("*******************Deleting page!*******************");
        service.deletePage(idPage);
        return ResponseEntity.status(HttpStatus.OK).body("Page deleted successfully!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/page/{id}")
    public ResponseEntity<String> updatePageById(@PathVariable Long id, @RequestBody UpdatePageDTO dto) {
        service.updatePage(id, dto);
        logger.info("*******************Updating page!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Page updated successfully!");
    }

    @GetMapping("/image/{idChapter}/{pageNumber}")
    public ResponseEntity<UrlResource> getPages(@PathVariable Long idChapter, @PathVariable Integer pageNumber) throws MalformedURLException {
        List<Pagina> paginas = service.getCapituloPorId(idChapter);
        paginas.sort(Comparator.comparingLong(Pagina::getId));

        Pagina paginaDaVez = paginas.get(pageNumber);

        Path path = Paths.get("uploads").resolve(paginaDaVez.getPathPage());
        UrlResource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @PutMapping("/update-progress")
    public void updateReadingProgress(@RequestBody UpdateChapterDTO updateChapterDTO) {
        service.updateReadingProgress(updateChapterDTO);
    }

    @GetMapping("/reading-progress/{idChapter}")
    public ResponseEntity<GetChapterDTO> getReadingProgress(@PathVariable Long idChapter) {
        Chapter chapterByID = service.getChapterByID(idChapter);
        GetChapterDTO getChapterDTO = new GetChapterDTO(chapterByID.getTitle(), null,
                chapterByID.getStatus(), chapterByID.getReadingProgress());
        return ResponseEntity.ok().body(getChapterDTO);
    }

}
