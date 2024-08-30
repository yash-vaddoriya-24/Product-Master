package com.example.productmaster.Controller;

import com.example.productmaster.Model.SubCategoryMaster;
import com.example.productmaster.Repository.SubCategoryRepository;
import com.example.productmaster.Service.SubCatergoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class SubCategoryController {

    @Autowired
    private SubCatergoryService subCatergoryService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;


    @PostMapping("/products/subcategory")
    @ResponseBody
    public ResponseEntity<List<String>> findAllCategories(){
        return ResponseEntity.ok(subCatergoryService.getAllCategories());
    }

    @PostMapping("/subcategory/save")
    public String saveSubCategory(@RequestParam("category") String categoryName,
                                  @RequestParam("subcategory") String subcategoryName,
                                  @RequestParam("cat_desc") String cat_desc,
                                  RedirectAttributes redirectAttributes) {
        try {
            subcategoryName = subcategoryName.toLowerCase().trim();
            if (!subCatergoryService.subCategoryExists(subcategoryName)  && !categoryName.isEmpty() && !subcategoryName.isEmpty() && !cat_desc.isEmpty()) {
                SubCategoryMaster subCatMaster = new SubCategoryMaster(categoryName, subcategoryName, cat_desc);
                subCatergoryService.addCategory(subCatMaster);
                redirectAttributes.addFlashAttribute("succesMessage", "Subcategory added successfully");
            } else {
                if(subCatergoryService.subCategoryExists(subcategoryName)) {
                    if(subCategoryRepository.existsByNameWithDelete(subcategoryName)){
                        int rowAffected = subCategoryRepository.updateDelete(subcategoryName);
                        if(rowAffected > 0) {
                            redirectAttributes.addFlashAttribute("succesMessage", "SubCategory added successfully");
                        }
                    }
                    else {
                        redirectAttributes.addFlashAttribute("errorMessage", "SubCategory already exists");
                    }
//                    redirectAttributes.addFlashAttribute("errorMessage", "Subcategory already exists");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/sub_category_master";
    }

    @GetMapping("/api/subcategories")
    @ResponseBody
    public List<SubCategoryMaster> findAllSubCategories() {
        System.out.println(subCatergoryService.getAllSubCategories());
        return subCatergoryService.getAllSubCategories();
    }

    @PutMapping("/products/subcategory/delete")
    public ResponseEntity<String> updateActiveStatus(@RequestParam("subcategoryId") String subcat_id){
        try {
            if(subcat_id != null) {
                subCatergoryService.updateActive(subcat_id);
                return ResponseEntity.ok("Subcategory deleted successfully");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Subcategory not found");
    }

    @PutMapping("/products/subcategory/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateDetails(@RequestParam("active") String activeStatus,
                                                            @RequestParam("subCategoryId") String cat_id,
                                                             @RequestParam("categoryName") String catName,
                                                             @RequestParam("subCatName") String subCatName,
                                                             @RequestParam("subCatDesc") String subCatDesc) {

        try {
            subCatName = subCatName.toLowerCase();
            Long subCatId = Long.parseLong(cat_id);
            Map<String, Object> response = new HashMap<>();
            response.put("name", catName);
            response.put("subCatName", subCatName);
            response.put("description", subCatDesc);

            if (subCatergoryService.subCategoryExists(subCatName)) {
                Long existingSubCatId = subCatergoryService.subCategoryIdbyName(subCatName);
                if (!existingSubCatId.equals(subCatId)) {
                    response.put("error", "Subcategory with the same name already exists");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }

            subCatergoryService.updateSubCategory(Boolean.parseBoolean(activeStatus), catName, subCatName, subCatDesc, subCatId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
