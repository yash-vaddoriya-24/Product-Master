package com.example.productmaster.Repository;

import com.example.productmaster.Model.CategoryMaster;
import com.example.productmaster.Model.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductMaster, Long> {

    boolean existsByName(String name);

    @Query("SELECT pm FROM ProductMaster pm WHERE pm.delete=false and pm.categoryMaster.active=true")
    List<ProductMaster> findByDelete(boolean delete);

    @Modifying
    @Transactional
    @Query("UPDATE ProductMaster pm SET pm.delete=true, pm.active = false WHERE pm.id =:Id")
    int deleteProductById(Long Id);


    @Query("SELECT pm FROM ProductMaster pm WHERE pm.id =:Id")
    List<ProductMaster> getValuesById(Long Id);

    @Modifying
    @Transactional
    @Query("UPDATE ProductMaster pm " +
            "SET pm.name = :#{#p.name}, " +
            "    pm.active= :#{#p.active}, " +
            "    pm.description = :#{#p.description}, " +
            "    pm.catName = :#{#p.catName}, " +
            "    pm.subCatName = :#{#p.subCatName}, " +
            "    pm.price = :#{#p.price}, " +
            "    pm.manuDate = :#{#p.manuDate}, " +
            "    pm.serialNo = :#{#p.serialNo}, " +
            "    pm.warranty = :#{#p.warranty}, " +
            "    pm.img = :#{#p.img}, " +
            "    pm.condition = :#{#p.condition}, " +
            "    pm.color = :#{#p.color}, " +
            "    pm.discount = :#{#p.discount}, " +
            "    pm.validFrom = :#{#p.validFrom}, " +
            "    pm.validTo = :#{#p.validTo} " +
            "WHERE pm.id = :#{#p.id}")
    int updateAllDetails(@Param("p") ProductMaster p);

    @Query("SELECT pm.id FROM ProductMaster pm WHERE pm.name = :pname")
    Integer findIdByName(String pname);

    @Query("SELECT pm.delete FROM ProductMaster pm WHERE pm.name = :pname")
    boolean existsByNameWithDelete(String pname);

    @Modifying
    @Transactional
    @Query("UPDATE ProductMaster pm set pm.delete = false, pm.active=true where pm.name = :pname")
    int updateDelete(String pname);
}
