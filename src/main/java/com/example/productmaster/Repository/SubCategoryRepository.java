package com.example.productmaster.Repository;

import com.example.productmaster.Model.CategoryMaster;
import com.example.productmaster.Model.SubCategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryMaster, Long> {

    boolean existsBysubcategoryName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE SubCategoryMaster cm SET cm.active = false, cm.delete = true WHERE cm.subcategoryId = :id")
    void updateSubCategory(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SubCategoryMaster cm SET cm.active = :activeStatus, cm.categoryName = :cat_name, cm.subcategoryName = :subcatName, cm.subcategoryDescription = :subcatDesc WHERE cm.subcategoryId = :id")
    int updateDetails(boolean activeStatus,String cat_name, String subcatName, String subcatDesc, Long id);

    @Query("select cm.subcategoryId from SubCategoryMaster as cm where cm.subcategoryName = :subcategory")
    Long findSubCategoryIdBySubcategory(String subcategory);

    @Query("SELECT cm FROM SubCategoryMaster cm WHERE cm.delete = false and cm.categoryMaster.active = true")
    List<SubCategoryMaster> findAllActive();

    @Query("SELECT cm.subcategoryName FROM SubCategoryMaster cm WHERE cm.active = true and cm.delete = false and cm.categoryName =:catName")
    List<String> findAllSubCategories(String catName);

    @Query("SELECT pm.delete FROM SubCategoryMaster pm WHERE pm.subcategoryName = :pname")
    boolean existsByNameWithDelete(String pname);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("UPDATE SubCategoryMaster pm set pm.delete = false, pm.active=true where pm.subcategoryName = :pname")
    int updateDelete(String pname);
}
