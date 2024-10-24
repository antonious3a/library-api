package dev.antonio3a.libraryapi.domain.services;

import dev.antonio3a.libraryapi.application.payloads.requests.BookRequest;
import dev.antonio3a.libraryapi.application.payloads.responses.BookResponse;
import dev.antonio3a.libraryapi.domain.entities.Book;
import dev.antonio3a.libraryapi.domain.repositories.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    private final AuthorService authorService;

    private Book loadBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book with id %s not found!".formatted(id)));
    }

    public BookResponse createBook(@Valid BookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        bookRequest.getAuthors().forEach(authorId -> book.getAuthors().add(authorService.loadAuthor(authorId)));
        return modelMapper.map(bookRepository.save(book), BookResponse.class);
    }

    public List<BookResponse> readBooks() {
        return bookRepository.findAll()
                .stream()
                .map(element -> modelMapper.map(element, BookResponse.class)).toList();
    }

    public BookResponse readBook(Long id) {
        return modelMapper.map(loadBook(id), BookResponse.class);
    }

    public BookResponse updateBook(Long id, @Valid BookRequest bookRequest) {
        Book book = loadBook(id);
        modelMapper.map(bookRequest, book);
        book.setAuthors(new LinkedHashSet<>());
        bookRequest.getAuthors().forEach(authorId -> book.getAuthors().add(authorService.loadAuthor(authorId)));
        return modelMapper.map(bookRepository.save(book), BookResponse.class);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(loadBook(id).getId());
    }
}
