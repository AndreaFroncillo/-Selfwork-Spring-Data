package it.aulab.blog.services;

import java.util.List;

import it.aulab.blog.dtos.PostDto;

public interface PostService {

    List<PostDto> readAll();

    PostDto read(Long id);

    List<PostDto> readByTitle(String title);

    PostDto create(PostDto postDto);

    PostDto update(Long id, PostDto postDto);

    void delete(Long id);
}