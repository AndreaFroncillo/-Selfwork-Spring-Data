package it.aulab.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.blog.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);

    List<Post> findByAuthorId(Long id);

    List<Post> findByAuthorName(String name);

    List<Post> findByTitleContaining(String keyword);

    @Query(value = "SELECT * FROM posts", nativeQuery = true)
    List<Post> findAllPostsNative();

    @Query(value = "SELECT * FROM posts WHERE title LIKE %:title%", nativeQuery = true)
    List<Post> findPostsByTitleNative(@Param("title") String title);

}