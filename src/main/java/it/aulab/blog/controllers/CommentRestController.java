package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.CommentDto;
import it.aulab.blog.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(commentService.read(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentDto>> getCommentsByUsername(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(
                commentService.readByUsername(username)
        );
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(commentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDto commentDto
    ) {
        return ResponseEntity.ok(
                commentService.update(id, commentDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id
    ) {
        commentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}