package com.example.productmaster.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean active;
    private String name;
    private String description;
    private boolean delete;

//    @OneToMany
//    @Getter@Setter
//    private List<SubCategoryMaster> subCategories;

    public CategoryMaster(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
        this.delete = false;
    }

}
