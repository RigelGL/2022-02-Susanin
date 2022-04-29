package ru.otus.spring.command;


import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;


@ShellComponent
@AllArgsConstructor
public class BookCommands {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @ShellMethod("Get all books")
    public String books() {
        StringBuilder response = new StringBuilder("Books:\n");
        for(var book : bookService.getAll()) {
            response.append(book.getId()).append(" ").append(book.getName()).append("\n");
        }
        return response.toString();
    }

    @ShellMethod("See book")
    public String seeBook(@ShellOption() long bookId) {
        Book book = bookService.getById(bookId);

        if(book == null) {
            return "Book is not exists";
        }

        StringBuilder str = new StringBuilder("Book \"");

        str.append(book.getName()).append("\"\n");
        str.append("Author: ");
        if(book.getAuthor() != null) {
            str.append(book.getAuthor().getName());
        }
        else {
            str.append("<no author>");
        }
        str.append("\n");

        str.append("Genres: ");

        for(int i = 0; i < book.getGenres().size(); i++) {
            str.append(book.getGenres().get(i).getName());
            if(i < book.getGenres().size() - 1)
                str.append(", ");
        }

        if(book.getGenres().size() == 0) {
            str.append("<no genres>");
        }

        str.append("\n");

        str.append("Content: ").append(book.getContent() == null ? "<no text>" : book.getContent()).append("\n");

        return str.toString();
    }

    @ShellMethod("Add genre to book")
    public String addGenreToBook(@ShellOption() long bookId, @ShellOption() String genreName) {
        Book book = bookService.getById(bookId);
        Genre genre = genreService.getByName(genreName);

        if(book == null) {
            return "Book not found";
        }

        if(genre == null) {
            return "Genre not found";
        }

        if(book.getGenres().contains(genre)) {
            return "Error: the book has the genre";
        }
        book.getGenres().add(genre);
        bookService.update(book);
        return "Genre added to book";
    }

    @ShellMethod("Remove genre from book")
    public String removeGenreFromBook(@ShellOption() long bookId, @ShellOption() String genreName) {
        Book book = bookService.getById(bookId);
        Genre genre = genreService.getByName(genreName);

        if(book == null) {
            return "Book not found";
        }

        if(genre == null) {
            return "Genre not found";
        }

        if(!book.getGenres().contains(genre)) {
            return "Error: the book does not have the gender";
        }

        book.getGenres().remove(genre);
        bookService.update(book);
        return "Genre removed from book";
    }

    @ShellMethod("Add book")
    public String addBook(@ShellOption() String name, @ShellOption() long authorId) {
        Author author = authorService.getById(authorId);

        if(author == null) {
            return "Author not found";
        }

        Book book = bookService.addBook(new Book(name, author));
        return "Book added";
    }

    @ShellMethod("Set book content")
    public String setBookContent(@ShellOption() long bookId, @ShellOption() String content) {
        Book book = bookService.getById(bookId);
        if(book == null) {
            return "Book not found";
        }

        boolean success = bookService.update(new Book(
                book.getId(),
                book.getName(),
                content,
                book.getAuthor(),
                book.getGenres()
        ));

        if(success) {
            return "Book content updated";
        }
        else {
            return "Error: book is not updated";
        }
    }

    @ShellMethod("Set book author")
    public String setBookAuthor(@ShellOption() long bookId, @ShellOption() long authorId) {
        Book book = bookService.getById(bookId);
        if(book == null) {
            return "Book not found";
        }

        Author author = authorService.getById(authorId);
        if(author == null) {
            return "Author not found";
        }

        boolean success = bookService.update(new Book(
                book.getId(),
                book.getName(),
                book.getContent(),
                author,
                book.getGenres()
        ));

        if(success) {
            return "Book author updated";
        }
        else {
            return "Error: book is not updated";
        }
    }

    @ShellMethod("Delete book")
    public String delBook(@ShellOption() long bookId) {
        boolean success = bookService.deleteById(bookId);

        if(success) {
            return "Book deleted";
        }
        else {
            return "Error: book is not deleted";
        }
    }
}
