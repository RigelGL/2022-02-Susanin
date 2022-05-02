package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
public class Book {

    private final long id;
    private final String name;
    private final String content;

    private final Author author;
    private final List<Genre> genres;

    public Book(String name, Author author) {
        this.name = name;
        this.author = author;
        id = 0;
        content = "";
        genres = new ArrayList<>();
    }
}
