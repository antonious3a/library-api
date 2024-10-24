package dev.antonio3a.libraryapi.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.REMOVE)
    private Set<Book> books = new LinkedHashSet<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return getId() != null && Objects.equals(getId(), author.getId());
    }

    @Override
    public final int hashCode() {
        return getId().hashCode();
    }
}
