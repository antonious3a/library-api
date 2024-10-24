package dev.antonio3a.libraryapi.application.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link dev.antonio3a.libraryapi.domain.entities.Book}
 */
@Value
public class BookRequest implements Serializable {

    @NotBlank
    String title;

    String isbn;

    @Size(min = 1)
    @NotNull
    Set<Long> authors;
}