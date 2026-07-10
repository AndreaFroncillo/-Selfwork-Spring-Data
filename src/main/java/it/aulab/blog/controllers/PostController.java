package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.models.Author;
import it.aulab.blog.models.Comment;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.AuthorRepository;
import it.aulab.blog.repositories.CommentRepository;
import it.aulab.blog.repositories.PostRepository;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(post))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {

        if (post.getAuthor() == null || post.getAuthor().getId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("È necessario specificare un autore.");
        }

        Author author = authorRepository.findById(post.getAuthor().getId()).orElse(null);

        if (author == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Autore non trovato.");
        }

        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        if (postDetails.getTitle() != null) {
            post.setTitle(postDetails.getTitle());
        }

        if (postDetails.getBody() != null) {
            post.setBody(postDetails.getBody());
        }

        if (postDetails.getAuthor() != null && postDetails.getAuthor().getId() != null) {

            Author author = authorRepository.findById(postDetails.getAuthor().getId()).orElse(null);

            if (author == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Autore non trovato.");
            }

            post.setAuthor(author);
        }

        Post updatedPost = postRepository.save(post);

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        List<Comment> comments = commentRepository.findByPostId(id);

        commentRepository.deleteAll(comments);

        postRepository.delete(post);

        return ResponseEntity.noContent().build();
    }
}