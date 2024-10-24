package dev.antonio3a.libraryapi.domain.repositories;

import dev.antonio3a.libraryapi.domain.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}