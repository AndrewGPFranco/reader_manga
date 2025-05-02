package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.Pagina;
import com.reader.manga.ports.repositories.PaginaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPaginaRepository extends PaginaRepository, JpaRepository<Pagina, Long> {

    @Query("SELECT p FROM Pagina p WHERE p.chapter.id = :id")
    List<Pagina> findByIdChapter(@Param("id") Long id);

    @Query("SELECT p.pathPage FROM Pagina p WHERE p.chapter.id = :idChapter")
    List<String> getUriPagina(@Param("idChapter") Long idChapter);

}