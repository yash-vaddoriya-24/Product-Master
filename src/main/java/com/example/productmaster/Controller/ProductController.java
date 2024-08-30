package com.example.productmaster.Controller;

import com.example.productmaster.Model.ProductMaster;
import com.example.productmaster.Repository.ProductRepository;
import com.example.productmaster.Service.CategoryService;
import com.example.productmaster.Service.ProductService;
import com.example.productmaster.Service.SubCatergoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCatergoryService subCatergoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/categories/product")
    @ResponseBody
    public ResponseEntity<List<String>> getAllCategories() {
        System.out.println(categoryService.getAllCategoryNames());
        return ResponseEntity.ok(categoryService.getAllCategoryNames());
    }

    @GetMapping("/api/subcategories/product")
    @ResponseBody
    public ResponseEntity<List<String>> getallSubCategories(@RequestParam("catName") String catName) {
        System.out.println(subCatergoryService.getAllSubCategoryNames(catName));
        return ResponseEntity.ok(subCatergoryService.getAllSubCategoryNames(catName));
    }

    //    @RequestParam String name, @RequestParam String description,@RequestParam String catName,@RequestParam String subCatName,@RequestParam String price,@RequestParam String manuDate, @RequestParam String serialNo,@RequestParam String warranty,@RequestParam String img,@RequestParam String condition,@RequestParam String color,@RequestParam String discount,@RequestParam String validFrom,@RequestParam String validTo
    @PostMapping("/api/addProducts")
    @ResponseBody
    public ResponseEntity<String> addProducts(@RequestBody ProductMaster pm, HttpSession session) {
        System.out.println(pm.getId());
        String pname = pm.getName().toLowerCase();
        Integer newId = productRepository.findIdByName(pname);
        System.out.println(productRepository.existsByNameWithDelete(pname));
        try {
            if (session != null) {
                if(pm.getId() != null) {
                    if (productRepository.existsByName(pm.getName().toLowerCase()) && newId != null && newId == Integer.parseInt(String.valueOf(pm.getId()))) {
                        int rowAffeted = updateDetails(pm);
                        if (rowAffeted > 0) {
                            return ResponseEntity.ok("Product updated successfully");
                        }
                    } else if (!productRepository.existsByName(pm.getName().toLowerCase())) {
                        int rowAffeted = updateDetails(pm);
                        if (rowAffeted > 0) {
                            return ResponseEntity.ok("Product updated successfully");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already exist");
                    }
                }else{
                    if (productService.existProduct(pname)) {
                        if(productRepository.existsByNameWithDelete(pname)){
                            int rowAffected = productRepository.updateDelete(pname);
                            if(rowAffected > 0) {
                                return ResponseEntity.ok("Product added successfully");
                            }
                        }
                        else{
                            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already exist");
                        }
//                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already exists");
                    }
                    else {
                        ProductMaster productMaster = new ProductMaster(true, pname, pm.getDescription(), pm.getCatName(), pm.getSubCatName(), pm.getPrice(), pm.getManuDate(), pm.getSerialNo(), pm.getWarranty(), pm.getImg(), pm.getCondition(), pm.getColor(), pm.getDiscount(), pm.getValidFrom(), pm.getValidTo());
                        productService.addProduct(productMaster);
                        return ResponseEntity.ok("Product added successfully");
                    }
                }

            } else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Login required");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error while adding product");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error while adding product");
    }

    @PostMapping("/api/getProducts")
    @ResponseBody
    public List<ProductMaster> getAllProducts() {
//        System.out.println(productService.getAllProductsByActive(true));
        return productService.getAllProductsByActive(false);
    }

    @PostMapping("/api/deleteProduct")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@RequestParam String productId, HttpSession session) {
        System.out.println(productId);
        try {
            if(session != null) {
                if (productId != null) {
                    int rowAffected = productRepository.deleteProductById(Long.valueOf(productId));
                    if (rowAffected > 0)
                        return ResponseEntity.ok("Product deleted successfully");
                    else
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error while deleting product");
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Product Id not exists");
                }
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Login required");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error while deleting product");
        }

    }

    @PostMapping("/api/getProductId")
    @ResponseBody
    public ResponseEntity<List<ProductMaster>> getValuesByProductId(@RequestParam("productId") String Id) {
        System.out.println(productRepository.getValuesById(Long.valueOf(Id)));
        return ResponseEntity.ok(productRepository.getValuesById(Long.valueOf(Id)));
    }

    public int updateDetails(ProductMaster p) {
        System.out.println(p);
        System.out.println(p.isActive());
        return productRepository.updateAllDetails(p);
    }

}
