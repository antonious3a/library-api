package dev.antonio3a.libraryapi.domain.services;

import dev.antonio3a.libraryapi.application.payloads.requests.AuthorRequest;
import dev.antonio3a.libraryapi.application.payloads.responses.AuthorResponse;
import dev.antonio3a.libraryapi.domain.entities.Author;
import dev.antonio3a.libraryapi.domain.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    public Author loadAuthor(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author with id %s not found!".formatted(id)));
    }

    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        return modelMapper.map(authorRepository.save(modelMapper.map(authorRequest, Author.class)), AuthorResponse.class);
    }

    public List<AuthorResponse> readAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(element -> modelMapper.map(element, AuthorResponse.class)).toList();
    }

    public AuthorResponse readAuthor(Long id) {
        return modelMapper.map(loadAuthor(id), AuthorResponse.class);
    }

    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
        Author author = loadAuthor(id);
        modelMapper.map(authorRequest, author);
        return modelMapper.map(authorRepository.save(author), AuthorResponse.class);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(loadAuthor(id).getId());
    }
}
