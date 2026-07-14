package it.aulab.blog.services;

import java.util.List;

import it.aulab.blog.dtos.AuthorDto;

public interface AuthorService {

    List<AuthorDto> readAll();

    AuthorDto read(Long id);

    List<AuthorDto> readByEmail(String email);

    List<AuthorDto> readByNameAndSurname(String name, String surname);

    AuthorDto create(AuthorDto authorDto);

    AuthorDto update(Long id, AuthorDto authorDto);

    void delete(Long id);
}