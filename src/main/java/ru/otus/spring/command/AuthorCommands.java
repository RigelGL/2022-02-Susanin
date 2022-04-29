package ru.otus.spring.command;


import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.AuthorService;


@ShellComponent
@AllArgsConstructor
public class AuthorCommands {

    private final AuthorService service;

    @ShellMethod("Show all authors")
    public String authors() {
        StringBuilder response = new StringBuilder("Authors:\n");
        for(var author : service.getAll()) {
            response.append(author.getId()).append(" ").append(author.getName()).append("\n");
        }
        return response.toString();
    }

    @ShellMethod("Add author")
    public String addAuthor(@ShellOption() String name) {
        Author author = service.addAuthor(new Author(0, name.strip()));
        if(author == null) {
            return "Error: author not added";
        } else {
            return "Author added";
        }
    }

    @ShellMethod("Delete author")
    public String delAuthor(@ShellOption() long id) {
        boolean deleted = service.deleteById(id);
        if(deleted) {
            return "Author deleted";
        } else {
            return "Such author is not exists";
        }
    }

    @ShellMethod("Rename author")
    public String renameAuthor(@ShellOption() long id, @ShellOption() String name) {
        Author author = service.getById(id);
        if(author == null) {
            return "Author not found";
        }

        boolean updated = service.update(new Author(id, name.strip()));

        if(updated) {
            return "Author updated";
        } else {
            return "Error, no author updated";
        }
    }
}
