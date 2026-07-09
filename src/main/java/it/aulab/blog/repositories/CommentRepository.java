package it.aulab.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.blog.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUsername(String username);

    List<Comment> findByPostId(Long id);

    @Query(value = "SELECT * FROM comments WHERE username = :username", nativeQuery = true)
    List<Comment> findCommentsByUsernameNative(@Param("username") String username);

}