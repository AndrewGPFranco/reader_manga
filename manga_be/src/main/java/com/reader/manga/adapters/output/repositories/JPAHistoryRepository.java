package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAHistoryRepository extends JpaRepository<History, UUID> {
}
