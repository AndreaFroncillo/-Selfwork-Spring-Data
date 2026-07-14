package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.AuthorDto;
import it.aulab.blog.services.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(authorService.read(id));
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<AuthorDto>> getAuthorsByEmail(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                authorService.readByEmail(email)
        );
    }

    @GetMapping("/search/fullname")
    public ResponseEntity<List<AuthorDto>> getAuthorsByFullname(
            @RequestParam String name,
            @RequestParam String surname
    ) {
        return ResponseEntity.ok(
                authorService.readByNameAndSurname(name, surname)
        );
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
            @RequestBody AuthorDto authorDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.create(authorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorDto authorDto
    ) {
        return ResponseEntity.ok(
                authorService.update(id, authorDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id
    ) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}