package com.reader.manga.repository;

import com.reader.manga.model.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaginaRepository extends JpaRepository<Pagina, Long> {

    @Query("SELECT p FROM Pagina p WHERE p.chapter.id = :id")
    List<Pagina> findByIdChapter(@Param("id") Long id);


}
