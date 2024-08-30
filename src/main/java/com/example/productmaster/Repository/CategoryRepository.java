package com.example.productmaster.Repository;

import com.example.productmaster.Model.CategoryMaster;
import com.example.productmaster.Model.SubCategoryMaster;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryMaster,Long> {

    // No need to declare save method here, JpaRepository already provides save method

    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE CategoryMaster cm SET cm.active = false, cm.delete = true WHERE cm.id = :id")
    void updateActiveById(@Param("id") Long id);

    @Query("SELECT cm FROM CategoryMaster cm WHERE cm.delete = false")
    List<CategoryMaster> findAllActive();

    @Modifying
    @Query("UPDATE CategoryMaster cm SET cm.name = :name, cm.description = :desc, cm.active = :active WHERE cm.id = :cat_id")
    int updateById(Long cat_id, String name, String desc, boolean active);

    @Modifying
    @Query("UPDATE SubCategoryMaster scm SET scm.active = :active where scm.categoryName = :name")
    int updateActiveByCategoryName(String name, boolean active);


    @Query("SELECT cm.id FROM CategoryMaster cm WHERE cm.name = :catName")
    Long findCategoryIdByName(@Param("catName") String categoryName);

    @Query("SELECT cm.name FROM CategoryMaster cm WHERE cm.active = true and cm.delete = false")
    List<String> findAllCategories();

    @Query("SELECT pm.delete FROM CategoryMaster pm WHERE pm.name = :pname")
    boolean existsByNameWithDelete(String pname);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("UPDATE CategoryMaster pm set pm.delete = false, pm.active=true where pm.name = :pname")
    int updateDelete(String pname);

}
