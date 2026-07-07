package it.aulab.blog;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.aulab.blog.models.Author;
import it.aulab.blog.models.Comment;
import it.aulab.blog.models.Post;
import it.aulab.blog.repositories.AuthorRepository;
import it.aulab.blog.repositories.CommentRepository;
import it.aulab.blog.repositories.PostRepository;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testAuthorDerivedQueries() {

        Author author = new Author();
        author.setName("Andrea");
        author.setSurname("Froncillo");
        author.setEmail("andrea@test.com");

        authorRepository.save(author);

        List<Author> authorsByName = authorRepository.findByName("Andrea");
        Author authorByEmail = authorRepository.findByEmail("andrea@test.com");

        assertFalse(authorsByName.isEmpty());
        assertNotNull(authorByEmail);
        assertEquals("Andrea", authorByEmail.getName());
    }

    @Test
    void testPostDerivedQueries() {

        Author author = new Author();
        author.setName("Mario");
        author.setSurname("Rossi");
        author.setEmail("mario@test.com");

        authorRepository.save(author);

        Post post = new Post();
        post.setTitle("Spring Data JPA");
        post.setBody("Articolo su Spring Data");
        post.setAuthor(author);

        postRepository.save(post);

        List<Post> postsByTitle = postRepository.findByTitle("Spring Data JPA");
        List<Post> postsContaining = postRepository.findByTitleContaining("Spring");

        assertFalse(postsByTitle.isEmpty());
        assertFalse(postsContaining.isEmpty());
        assertEquals("Spring Data JPA", postsByTitle.get(0).getTitle());
    }

    @Test
    void testCommentDerivedQueries() {

        Author author = new Author();
        author.setName("Luca");
        author.setSurname("Verdi");
        author.setEmail("luca@test.com");

        authorRepository.save(author);

        Post post = new Post();
        post.setTitle("Hibernate");
        post.setBody("Articolo su Hibernate");
        post.setAuthor(author);

        postRepository.save(post);

        Comment comment = new Comment();
        comment.setUsername("Giulia");
        comment.setBody("Bellissimo articolo!");
        comment.setPost(post);

        commentRepository.save(comment);

        List<Comment> comments = commentRepository.findByUsername("Giulia");

        assertFalse(comments.isEmpty());
        assertEquals("Giulia", comments.get(0).getUsername());
    }

    @Test
    void testNativeQueryPosts() {

        Author author = new Author();
        author.setName("Sara");
        author.setSurname("Bianchi");
        author.setEmail("sara@test.com");

        authorRepository.save(author);

        Post post = new Post();
        post.setTitle("Query Native");
        post.setBody("Test query native");
        post.setAuthor(author);

        postRepository.save(post);

        List<Post> posts = postRepository.findAllPostsNative();

        assertFalse(posts.isEmpty());
    }
}