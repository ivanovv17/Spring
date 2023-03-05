package com.example.demo.services;

import com.example.demo.entities.Author;
import org.springframework.stereotype.Service;


public interface AuthorService {
    Author getRandomAuthor();
}
