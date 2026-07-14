package it.aulab.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.blog.dtos.PostDto;
import it.aulab.blog.models.Author;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.AuthorRepository;
import it.aulab.blog.repositories.PostRepository;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper mapper;

    private PostDto convertToDto(Post post) {
        PostDto dto = mapper.map(post, PostDto.class);

        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorFullname(
                    post.getAuthor().getName()
                            + " "
                            + post.getAuthor().getSurname()
            );
        }

        dto.setNumberOfComments(post.getComments().size());

        return dto;
    }

    @Override
    public List<PostDto> readAll() {
        List<PostDto> dtos = new ArrayList<>();

        for (Post post : postRepository.findAll()) {
            dtos.add(convertToDto(post));
        }

        return dtos;
    }

    @Override
    public PostDto read(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post con id " + id + " non trovato"
                ));

        return convertToDto(post);
    }

    @Override
    public List<PostDto> readByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Titolo obbligatorio"
            );
        }

        List<PostDto> dtos = new ArrayList<>();

        for (Post post : postRepository.findByTitleContaining(title)) {
            dtos.add(convertToDto(post));
        }

        return dtos;
    }

    @Override
    public PostDto create(PostDto postDto) {
        if (postDto.getTitle() == null
                || postDto.getTitle().isBlank()
                || postDto.getBody() == null
                || postDto.getBody().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Titolo e contenuto sono obbligatori"
            );
        }

        if (postDto.getAuthorId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "È necessario specificare un autore"
            );
        }

        Author author = authorRepository.findById(postDto.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Autore non trovato"
                ));

        Post post = mapper.map(postDto, Post.class);
        post.setId(null);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        return convertToDto(savedPost);
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post con id " + id + " non trovato"
                ));

        if (postDto.getTitle() != null && !postDto.getTitle().isBlank()) {
            post.setTitle(postDto.getTitle());
        }

        if (postDto.getBody() != null && !postDto.getBody().isBlank()) {
            post.setBody(postDto.getBody());
        }

        if (postDto.getAuthorId() != null) {
            Author author = authorRepository.findById(postDto.getAuthorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Autore non trovato"
                    ));

            post.setAuthor(author);
        }

        return convertToDto(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post con id " + id + " non trovato"
                ));

        postRepository.delete(post);
    }
}