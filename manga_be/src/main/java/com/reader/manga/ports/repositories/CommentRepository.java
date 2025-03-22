package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.mangas.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    void deleteAll();

    List<Comment> findAll();

    void deleteById(Long id);

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

}