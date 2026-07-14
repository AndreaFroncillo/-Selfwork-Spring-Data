package it.aulab.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.blog.dtos.AuthorDto;
import it.aulab.blog.models.Author;
import it.aulab.blog.repositories.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper mapper;

    private AuthorDto convertToDto(Author author) {
        AuthorDto dto = mapper.map(author, AuthorDto.class);

        dto.setFullname(author.getName() + " " + author.getSurname());
        dto.setNumberOfPosts(author.getPosts().size());

        return dto;
    }

    @Override
    public List<AuthorDto> readAll() {
        List<AuthorDto> dtos = new ArrayList<>();

        for (Author author : authorRepository.findAll()) {
            dtos.add(convertToDto(author));
        }

        return dtos;
    }

    @Override
    public AuthorDto read(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autore con id " + id + " non trovato"
                ));

        return convertToDto(author);
    }

    @Override
    public List<AuthorDto> readByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email obbligatoria"
            );
        }

        List<AuthorDto> dtos = new ArrayList<>();
        Author author = authorRepository.findByEmail(email);

        if (author != null) {
            dtos.add(convertToDto(author));
        }

        return dtos;
    }

    @Override
    public List<AuthorDto> readByNameAndSurname(
            String name,
            String surname
    ) {
        if (name == null || name.isBlank()
                || surname == null || surname.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome e cognome sono obbligatori"
            );
        }

        List<AuthorDto> dtos = new ArrayList<>();

        for (Author author :
                authorRepository.findByNameAndSurname(name, surname)) {
            dtos.add(convertToDto(author));
        }

        return dtos;
    }

    @Override
    public AuthorDto create(AuthorDto authorDto) {
        if (authorDto.getName() == null
                || authorDto.getName().isBlank()
                || authorDto.getSurname() == null
                || authorDto.getSurname().isBlank()
                || authorDto.getEmail() == null
                || authorDto.getEmail().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome, cognome ed email sono obbligatori"
            );
        }

        Author existingAuthor =
                authorRepository.findByEmail(authorDto.getEmail());

        if (existingAuthor != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email già presente nel database"
            );
        }

        Author author = mapper.map(authorDto, Author.class);
        author.setId(null);

        Author savedAuthor = authorRepository.save(author);

        return convertToDto(savedAuthor);
    }

    @Override
    public AuthorDto update(Long id, AuthorDto authorDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autore con id " + id + " non trovato"
                ));

        if (authorDto.getName() != null
                && !authorDto.getName().isBlank()) {
            author.setName(authorDto.getName());
        }

        if (authorDto.getSurname() != null
                && !authorDto.getSurname().isBlank()) {
            author.setSurname(authorDto.getSurname());
        }

        if (authorDto.getEmail() != null
                && !authorDto.getEmail().isBlank()) {

            Author existingAuthor =
                    authorRepository.findByEmail(authorDto.getEmail());

            if (existingAuthor != null
                    && !existingAuthor.getId().equals(id)) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email già presente nel database"
                );
            }

            author.setEmail(authorDto.getEmail());
        }

        return convertToDto(authorRepository.save(author));
    }

    @Override
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autore con id " + id + " non trovato"
                ));

        authorRepository.delete(author);
    }
}