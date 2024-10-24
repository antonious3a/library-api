package dev.antonio3a.libraryapi.application.controllers;

import dev.antonio3a.libraryapi.application.payloads.requests.BookRequest;
import dev.antonio3a.libraryapi.application.payloads.responses.BookResponse;
import dev.antonio3a.libraryapi.domain.services.BookService;
import dev.antonio3a.libraryapi.infrastructure.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@SecurityRequirements(value = {
        @SecurityRequirement(name = "JWT"),
        @SecurityRequirement(name = "OAUTH2")
})
@Tag(name = "Books", description = "Operations related to books")
public class BookController {

    private final BookService bookService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public BookResponse createBook(@RequestBody @Valid BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Read all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found"),
    })
    public List<BookResponse> readBooks() {
        return bookService.readBooks();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Read a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public BookResponse readBook(@PathVariable @Parameter(description = "Id of the book") Long id) {
        return bookService.readBook(id);
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Book updated"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public BookResponse updateBook(@PathVariable @Parameter(description = "Id of the book") Long id, @RequestBody @Valid BookRequest bookRequest) {
        return bookService.updateBook(id, bookRequest);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable @Parameter(description = "Id of the book") Long id) {
        bookService.deleteBook(id);
    }
}
