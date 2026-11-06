package com.de201028.mvc.service;

import com.de201028.mvc.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer categoryId);
}