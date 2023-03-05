package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BooksRepository;
import com.example.demo.repositories.CategoriesRepository;
import jakarta.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {
    private static final String RESOURCE_PATH = "src/main/resources";
    private static final String AUTHORS_FILE_PATH = RESOURCE_PATH + "/files/authors.txt";
    private static final String BOOKS_FILE_PATH = RESOURCE_PATH + "/files/books.txt";
    private static final String CATEGORIES_FILE_PATH = RESOURCE_PATH + "/files/categories.txt";
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    public SeedServiceImpl() {
    }

    @Override
    public void seedAuthors() throws IOException {
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .stream()
                .filter(s -> !s.isBlank())
                .map(s -> s.split(" "))
                .map(names -> new Author(names[0], names[1]))
                .forEach(authorRepository::save);
    }

    @Override
    public void seedCategories() throws IOException {
        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(s -> !s.isBlank())
                .map(s -> new Category(s))
                .forEach(categoriesRepository::save);
    }

    @Override
    public void seedBooks() throws IOException {
        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .stream()
                .filter(s -> !s.isBlank())
                .map(this::getBookObject)
                .forEach(booksRepository::save);
    }

    private Book getBookObject(String line) {
        String[] bookParts = line.split(" ");
        int editionTypeIndex = Integer.parseInt(bookParts[0]);
        EditionType editionType = EditionType.values()[editionTypeIndex];
        LocalDate publishDate = LocalDate.parse(bookParts[1], DateTimeFormatter.ofPattern("" +
                "d/M/yyyy"));
        int copies = Integer.parseInt(bookParts[2]);
        BigDecimal price = new BigDecimal(bookParts[3]);
        AgeRestriction ageRestriction = AgeRestriction.values()
                [Integer.parseInt(bookParts[4])];
        String title =
                Arrays.stream(bookParts)
                        .skip(5)
                        .collect(Collectors.joining(" "));
        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService.getRandomCategories();
        return new Book(title, editionType, price, copies, publishDate,
                ageRestriction, author, categories);
    }




}
