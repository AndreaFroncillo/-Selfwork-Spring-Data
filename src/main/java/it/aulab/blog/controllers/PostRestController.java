package it.aulab.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.PostDto;
import it.aulab.blog.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.read(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> getPostsByTitle(
            @RequestParam String title
    ) {
        return ResponseEntity.ok(
                postService.readByTitle(title)
        );
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.create(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostDto postDto
    ) {
        return ResponseEntity.ok(
                postService.update(id, postDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id
    ) {
        postService.delete(id);

        return ResponseEntity.noContent().build();
    }
}