package ru.otus.spring.domain;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Author {

    private final long id;
    private final String name;
}
