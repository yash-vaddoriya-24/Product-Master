package com.example.productmaster.Repository;

import com.example.productmaster.Model.RegisterMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterMaster, Long> {

    boolean existsByEmail(String email);
     boolean existsByUname(String username);

     @Query("SELECT rm.password FROM RegisterMaster rm WHERE rm.uname =:uname")
    String checkPassword(String uname);
}
