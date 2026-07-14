package it.aulab.blog.services;

import java.util.List;

import it.aulab.blog.dtos.CommentDto;

public interface CommentService {

    List<CommentDto> readAll();

    CommentDto read(Long id);

    List<CommentDto> readByUsername(String username);

    CommentDto create(CommentDto commentDto);

    CommentDto update(Long id, CommentDto commentDto);

    void delete(Long id);
}