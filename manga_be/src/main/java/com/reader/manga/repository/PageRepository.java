package com.reader.manga.repository;

import com.reader.manga.model.PageChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageChapter, Long> {
}
