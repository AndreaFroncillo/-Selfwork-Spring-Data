package it.aulab.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.blog.dtos.CommentDto;
import it.aulab.blog.models.Comment;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.CommentRepository;
import it.aulab.blog.repositories.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = mapper.map(comment, CommentDto.class);

        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
            dto.setPostTitle(comment.getPost().getTitle());
        }

        return dto;
    }

    @Override
    public List<CommentDto> readAll() {
        List<CommentDto> dtos = new ArrayList<>();

        for (Comment comment : commentRepository.findAll()) {
            dtos.add(convertToDto(comment));
        }

        return dtos;
    }

    @Override
    public CommentDto read(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Commento con id " + id + " non trovato"));

        return convertToDto(comment);
    }

    @Override
    public List<CommentDto> readByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username obbligatorio");
        }

        List<CommentDto> dtos = new ArrayList<>();

        for (Comment comment : commentRepository.findByUsername(username)) {
            dtos.add(convertToDto(comment));
        }

        return dtos;
    }

    @Override
    public CommentDto create(CommentDto commentDto) {
        if (commentDto.getUsername() == null
                || commentDto.getUsername().isBlank()
                || commentDto.getBody() == null
                || commentDto.getBody().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username e contenuto sono obbligatori");
        }

        if (commentDto.getPostId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "È necessario specificare un post");
        }

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Post non trovato"));

        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setId(null);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    @Override
    public CommentDto update(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Commento con id " + id + " non trovato"));

        if (commentDto.getUsername() != null
                && !commentDto.getUsername().isBlank()) {
            comment.setUsername(commentDto.getUsername());
        }

        if (commentDto.getBody() != null
                && !commentDto.getBody().isBlank()) {
            comment.setBody(commentDto.getBody());
        }

        if (commentDto.getPostId() != null) {
            Post post = postRepository.findById(commentDto.getPostId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Post non trovato"));

            comment.setPost(post);
        }

        return convertToDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Commento con id " + id + " non trovato"));

        commentRepository.delete(comment);
    }
}