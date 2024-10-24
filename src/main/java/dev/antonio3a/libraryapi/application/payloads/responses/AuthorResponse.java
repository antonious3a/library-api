package dev.antonio3a.libraryapi.application.payloads.responses;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link dev.antonio3a.libraryapi.domain.entities.Author}
 */
@Data
public class AuthorResponse implements Serializable {
    private Long id;

    private String name;

    private Integer age;
}