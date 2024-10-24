package dev.antonio3a.libraryapi.application.payloads.responses;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link dev.antonio3a.libraryapi.domain.entities.Book}
 */
@Data
public class BookResponse implements Serializable {
    private Long id;

    private String title;

    private String isbn;
}