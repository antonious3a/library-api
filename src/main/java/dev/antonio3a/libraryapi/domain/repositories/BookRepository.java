package dev.antonio3a.libraryapi.domain.repositories;

import dev.antonio3a.libraryapi.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}