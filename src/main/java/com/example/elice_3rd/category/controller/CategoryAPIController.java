package com.example.elice_3rd.category.controller;

import com.example.elice_3rd.category.entity.Category;
import com.example.elice_3rd.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryAPIController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> retrieveAllCategory(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
