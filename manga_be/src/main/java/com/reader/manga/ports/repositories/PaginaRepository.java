package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.mangas.Pagina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaginaRepository {

    Pagina save(Pagina pagina);

    Optional<Pagina> findById(Long id);

    List<Pagina> findByIdChapter(Long id);

    Page<Pagina> findAll(Pageable pageable);

}