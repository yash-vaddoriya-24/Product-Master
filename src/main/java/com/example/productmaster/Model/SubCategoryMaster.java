package com.example.productmaster.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;
    private boolean active;
    private String categoryName;
    private String subcategoryName;
    private String subcategoryDescription;
    private boolean delete;

    @ManyToOne
    private CategoryMaster categoryMaster;

    public SubCategoryMaster(String categoryName, String subcategoryName, String subcategoryDescription) {
        this.active = true;
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.subcategoryDescription = subcategoryDescription;
        this.delete = false;
        this.categoryMaster = new CategoryMaster();
    }

}
