package com.example.productmaster.Service;

import com.example.productmaster.Model.ProductMaster;
import com.example.productmaster.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductMaster addProduct(ProductMaster product) {
        return productRepository.save(product);
    }

    public boolean existProduct(String pname) {
        return productRepository.existsByName(pname);
    }

    public List<ProductMaster> getAllProductsByActive(boolean delete) {
        return productRepository.findByDelete(delete);
    }

}
