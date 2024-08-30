package com.example.productmaster.Service;

import com.example.productmaster.Model.CategoryMaster;
import com.example.productmaster.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryMaster addCategory(CategoryMaster category) {
        return categoryRepository.save(category);
    }

    public boolean categoryExists(String categoryName) {
        return categoryRepository.existsByName(categoryName);
    }

    public List<CategoryMaster> getAllCategories() {
        return categoryRepository.findAllActive();
    }

    public void deleteCategory(String categoryId) {
        categoryRepository.updateActiveById(Long.valueOf(categoryId));
    }

    public void updateCategory(String cat_id, String category_name, String category_description, boolean active) {
        categoryRepository.updateById(Long.valueOf(cat_id), category_name, category_description, active);
        categoryRepository.updateActiveByCategoryName(category_name, active);
    }

    public int getCategoryIdByName(String categoryName) {
        return Math.toIntExact(categoryRepository.findCategoryIdByName(categoryName));
    }

    public List<String> getAllCategoryNames() {
        return categoryRepository.findAllCategories();
    }
}
