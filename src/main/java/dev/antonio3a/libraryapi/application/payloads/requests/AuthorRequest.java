package dev.antonio3a.libraryapi.application.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.antonio3a.libraryapi.domain.entities.Author}
 */
@Value
public class AuthorRequest implements Serializable {
    @NotBlank
    String name;

    Integer age;
}