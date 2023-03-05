package com.example.demo.services;

import com.example.demo.entities.Category;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface CategoryService {
    Set<Category> getRandomCategories();
}
