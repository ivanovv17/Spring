package com.example.demo;

import com.example.demo.entities.Author;
import com.example.demo.entities.Book;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BooksRepository;
import com.example.demo.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public ConsoleRunner(SeedService seedService, BooksRepository booksRepository, AuthorRepository authorRepository) {
        this.seedService = seedService;
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
    }

    private void _04_AllBooksFromAuthorOrderedByReleaseDateDescAndBookTitleAsc(){
        List<Author> authors = this.authorRepository.findAll();
        Author author = authors.stream().filter(a -> a.getFirstName().equals("George") && a.getLastName().equals("Powell"))
                .findFirst()
                .orElse(null);
        if (author != null) {
            Set<Book> books = author.getBooks();
            Comparator<Book> compareByName = Comparator
                    .comparing(Book::getReleaseDate).reversed()
                    .thenComparing(Book::getTitle);
            books
                    .stream()
                    .sorted(compareByName)
                    .forEach(a-> System.out.printf("%s, %s, %d%n", a.getTitle(), a.getReleaseDate().toString(), a.getCopies()));
        } else {
            System.out.println("Author not found");
        }
    }


    private void _03_allAuthorsOrderedByBookCount() {
        List<Author> authors = this.authorRepository.findAll();
        authors
                .stream()
                .sorted((l,r) -> r.getBooks().size() - l.getBooks().size())
                .forEach(author -> System.out.printf("%s %s -> %d%n", author.getFirstName(), author.getLastName(),
                        author.getBooks().size()));

    }

    private void _02_printAllAuthorsWithAtLeastOneBookWithReleaseDateAfter1990() {
        LocalDate year1990 = LocalDate.of(1990, 1, 1);
        List<Author> authors = this.authorRepository.findDistinctByBooksReleaseDateBefore(year1990);
        authors.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
    }

    private void _01_booksAfter2000() {  //print all books after 2000 year;
        LocalDate year2000 = LocalDate.of(2000, 1, 1);
        List<Book> books = booksRepository.findAllByReleaseDateAfter(year2000);
        books.stream().forEach(b -> System.out.println(b.getReleaseDate() + " " + b.getTitle()));


    }

    @Override
    public void run(String... args) throws Exception {
        // seedService.seedAll(); //to add the information to the database
        //this._01_booksAfter2000();
        //this._02_printAllAuthorsWithAtLeastOneBookWithReleaseDateAfter1990();
        //this._03_allAuthorsOrderedByBookCount();
       // this._04_AllBooksFromAuthorOrderedByReleaseDateDescAndBookTitleAsc();
    }
}
