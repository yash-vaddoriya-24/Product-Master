package com.example.productmaster.Controller;

import com.example.productmaster.Model.RegisterMaster;
import com.example.productmaster.Repository.RegisterRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpResponse;

@Controller
public class SignUpController {
    @Autowired
    RegisterRepository registerRepository;


    @PostMapping("signup/addUser")
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody RegisterMaster rm){
        try{
           if(registerRepository.existsByEmail(rm.getEmail())){
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email Already Exists");
           }
           else if(registerRepository.existsByUname(rm.getUname().toLowerCase())){
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Username Already Exists");
           }
           else{
               registerRepository.save(rm);
               return ResponseEntity.ok("Successfully Registered");
           }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occuring while adding user");
        }
    }
}
