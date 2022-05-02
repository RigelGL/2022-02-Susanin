package ru.otus.spring.command;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.GenreService;


@ShellComponent
@AllArgsConstructor
public class GenreCommands {

    private final GenreService service;

    @ShellMethod("Show all genres")
    public String genres() {
        StringBuilder response = new StringBuilder("Genres:\n");
        for(var genre : service.getAll()) {
            response.append(genre.getName()).append("\n");
        }

        return response.toString();
    }

    @ShellMethod("Add genre")
    public String addGenre(@ShellOption() String name) {
        Genre inserted = service.addGenre(new Genre(0, name.strip()));
        if(inserted == null) {
            return "Genre with this name exists!";
        }
        else {
            return "Genre added";
        }
    }

    @ShellMethod("Delete genre")
    public String delGenre(@ShellOption() String name) {
        boolean deleted = service.deleteByName(name.strip());
        if(deleted) {
            return "Genre deleted";
        }
        else {
            return "Such genre is not exists";
        }
    }

    @ShellMethod("Update genre")
    public String renameGenre(@ShellOption() String from, @ShellOption() String to) {
        Genre genre = service.getByName(from.strip());
        if(genre == null) {
            return "Genre not found";
        }

        boolean updated = service.update(new Genre(genre.getId(), to.strip()));

        if(updated) {
            return "Genre updated";
        } else {
            return "Error: genre name duplicates. No genre updated.";
        }
    }

}
