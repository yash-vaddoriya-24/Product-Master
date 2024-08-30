package com.example.productmaster.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class ProductMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean active;
    private String name;
    private String description;
    private String catName;
    private String subCatName;
    private String price;
    private String manuDate;
    private String serialNo;
    private String warranty;
    private String img;
    private String condition;
    private String color;
    private String discount;
    private String validFrom;
    private String validTo;
    private boolean delete;

    @ManyToOne
    private CategoryMaster categoryMaster;

    public ProductMaster(boolean active, String name,String description, String catName,String subCatName,String price, String manuDate, String serialNo, String warranty, String img, String condition, String color, String discount, String validFrom, String validTo) {
        this.active = active;
        this.name = name;
        this.description = description;
        this.catName = catName;
        this.subCatName = subCatName;
        this.price = price;
        this.manuDate = manuDate;
        this.serialNo = serialNo;
        this.warranty = warranty;
        this.img = img;
        this.condition = condition;
        this.color = color;
        this.discount = discount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.delete = false;
    }

}
