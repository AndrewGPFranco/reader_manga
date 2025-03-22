package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.Comment;
import com.reader.manga.ports.repositories.CommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCommentRepository extends CommentRepository, JpaRepository<Comment, Long> {}