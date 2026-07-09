package it.aulab.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.blog.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByName(String name);

    List<Author> findByNameContaining(String name);

    List<Author> findBySurname(String surname);

    Author findByEmail(String email);

    @Query(value = "SELECT * FROM authors WHERE surname = :surname", nativeQuery = true)
    List<Author> findAuthorsBySurnameNative(@Param("surname") String surname);
}