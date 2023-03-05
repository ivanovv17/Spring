package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoryServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Set<Category> getRandomCategories() {
        long size = this.categoriesRepository.count();
        Random random = new Random();

        int categoriesCount = random.nextInt((int) size) +1;
        Set<Integer> categoriesIds = new HashSet<>();

        for (int i = 0; i < categoriesCount; i++) {
            int nextId = random.nextInt((int) size) +1;

            categoriesIds.add(nextId);
        }

        List<Category> allById = this.categoriesRepository.findAllById(categoriesIds);
        return new HashSet<>(allById);
    }
}
