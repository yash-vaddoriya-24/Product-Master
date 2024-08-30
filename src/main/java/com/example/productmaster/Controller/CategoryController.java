package com.example.productmaster.Controller;

import com.example.productmaster.Model.CategoryMaster;
import com.example.productmaster.Repository.CategoryRepository;
import com.example.productmaster.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;


    @DeleteMapping("/api/categories/delete")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> deleteCategory(@RequestParam("categoryId") String categoryId, RedirectAttributes redirectAttributes) {
        try {
            if (categoryId != null) {
                categoryService.deleteCategory(categoryId);
                redirectAttributes.addFlashAttribute("deleteMessage", "Category deleted successfully");
                return ResponseEntity.ok("Category deleted successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/api/categories/update")
    @Transactional
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCategory(
            @RequestParam("categoryId") String categoryId,
            @RequestParam("active") String active,
            @RequestParam("cat_N") String categoryName,
            @RequestParam("cat_D") String categoryDesc,
            RedirectAttributes redirectAttributes) {

        try {
            categoryName = categoryName.toLowerCase();
            Long categoryIdLong = Long.parseLong(categoryId);
            Map<String, Object> response = new HashMap<>();
            response.put("name", categoryName);
            response.put("description", categoryDesc);
            response.put("active", Boolean.parseBoolean(active));
            // Check if a category with the new name already exists
            if (categoryService.categoryExists(categoryName) && categoryIdLong != null) {
                Long existingCategoryId = (long) categoryService.getCategoryIdByName(categoryName);

                // Check if the found category ID matches the provided categoryId
                if (existingCategoryId.equals(categoryIdLong)) {
                    categoryService.updateCategory(categoryId, categoryName, categoryDesc, Boolean.parseBoolean(active));
                    redirectAttributes.addFlashAttribute("updateMessage", "Category updated successfully");
                    return ResponseEntity.ok(response);
                }
            }

            if(!categoryService.categoryExists(categoryName)){
                categoryService.updateCategory(categoryId, categoryName, categoryDesc, Boolean.parseBoolean(active));
                redirectAttributes.addFlashAttribute("updateMessage", "Category updated successfully");
                return ResponseEntity.ok(response);
            }
            // If category with the same name exists or IDs don't match
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/api/categories")
    @ResponseBody
    public List<CategoryMaster> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/saveCategory")
    public String saveCategory(
                               @RequestParam("category_name") String categoryName,
                               @RequestParam("category_description") String categoryDesc,
                               RedirectAttributes redirectAttributes) {
        try {
            categoryName = categoryName.toLowerCase();
            if (categoryService.categoryExists(categoryName)) {
                if(categoryRepository.existsByNameWithDelete(categoryName)){
                    int rowAffected = categoryRepository.updateDelete(categoryName);
                    if(rowAffected > 0) {
                        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully");
                    }
                }
                else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Category already exists");
                }
            } else {
                if (!categoryDesc.isEmpty()) {
                    CategoryMaster cat1 = new CategoryMaster(categoryName, categoryDesc);
                    CategoryMaster savedCategory = categoryService.addCategory(cat1);
                    if (savedCategory != null) {
                        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully");
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage", "Failed to add category");
                    }
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error occurred while adding category: ");
        }
        return "redirect:/category_master"; // Redirect to the category_master page
    }
}
