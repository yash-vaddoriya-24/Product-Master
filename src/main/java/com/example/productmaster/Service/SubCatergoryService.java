package com.example.productmaster.Service;

import com.example.productmaster.Model.SubCategoryMaster;
import com.example.productmaster.Repository.CategoryRepository;
import com.example.productmaster.Repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SubCatergoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public List<String> getAllCategories(){
        return categoryRepository.findAllCategories();
    }

    public void addCategory(SubCategoryMaster subCategoryMaster){
        subCategoryRepository.save(subCategoryMaster);
    }

    public boolean subCategoryExists(String subCategoryName){
        return subCategoryRepository.existsBysubcategoryName(subCategoryName);
    }

    public List<SubCategoryMaster> getAllSubCategories(){
        return subCategoryRepository.findAllActive();
    }

    public void updateActive(String id){
        subCategoryRepository.updateSubCategory(Long.valueOf(id));
    }

    public int updateSubCategory(boolean active, String cat_name, String subcatName, String subcatDesc, Long id){
        return subCategoryRepository.updateDetails(active,cat_name,subcatName,subcatDesc,id);
    }

    public Long subCategoryIdbyName(String subcatName){
        return subCategoryRepository.findSubCategoryIdBySubcategory(subcatName);
    }

    public List<String> getAllSubCategoryNames(String category) {
        return subCategoryRepository.findAllSubCategories(category);
    }
}
