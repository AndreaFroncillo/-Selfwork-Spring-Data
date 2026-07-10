package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.models.Comment;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.CommentRepository;
import it.aulab.blog.repositories.PostRepository;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {

        return commentRepository.findById(id)
                .map(comment -> ResponseEntity.ok(comment))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {

        if (comment.getPost() == null || comment.getPost().getId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("È necessario specificare un post.");
        }

        Post post = postRepository.findById(comment.getPost().getId()).orElse(null);

        if (post == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Post non trovato.");
        }

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        if (commentDetails.getUsername() != null) {
            comment.setUsername(commentDetails.getUsername());
        }

        if (commentDetails.getBody() != null) {
            comment.setBody(commentDetails.getBody());
        }

        if (commentDetails.getPost() != null && commentDetails.getPost().getId() != null) {

            Post post = postRepository.findById(commentDetails.getPost().getId()).orElse(null);

            if (post == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Post non trovato.");
            }

            comment.setPost(post);
        }

        Comment updatedComment = commentRepository.save(comment);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        commentRepository.delete(comment);

        return ResponseEntity.noContent().build();
    }
}