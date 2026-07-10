package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.models.Author;
import it.aulab.blog.models.Comment;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.AuthorRepository;
import it.aulab.blog.repositories.CommentRepository;
import it.aulab.blog.repositories.PostRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {

        return authorRepository.findById(id)
                .map(author -> ResponseEntity.ok(author))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody Author author) {

        Author existingAuthor = authorRepository.findByEmail(author.getEmail());

        if (existingAuthor != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Email già presente nel database");
        }

        Author savedAuthor = authorRepository.save(author);

        return ResponseEntity
                .status(201)
                .body(savedAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {

        Author author = authorRepository.findById(id).orElse(null);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }

        if (authorDetails.getName() != null) {
            author.setName(authorDetails.getName());
        }

        if (authorDetails.getSurname() != null) {
            author.setSurname(authorDetails.getSurname());
        }

        if (authorDetails.getEmail() != null) {

            Author existingAuthor = authorRepository.findByEmail(authorDetails.getEmail());

            if (existingAuthor != null && !existingAuthor.getId().equals(author.getId())) {
                return ResponseEntity
                        .badRequest()
                        .body("Email già presente nel database");
            }

            author.setEmail(authorDetails.getEmail());
        }

        Author updatedAuthor = authorRepository.save(author);

        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {

        Author author = authorRepository.findById(id).orElse(null);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }

        List<Post> posts = postRepository.findByAuthorId(id);

        for (Post post : posts) {

            List<Comment> comments = commentRepository.findByPostId(post.getId());

            commentRepository.deleteAll(comments);
            postRepository.delete(post);
        }

        authorRepository.delete(author);

        return ResponseEntity.noContent().build();
    }
}