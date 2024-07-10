package com.felix.DrugTracker.controller;

import com.felix.DrugTracker.entity.Block;
import com.felix.DrugTracker.entity.Product;
import com.felix.DrugTracker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            System.out.println("In the add product code ");

            System.out.println(product.getName() + " " + product.getSerialNumber());
            Product savedProduct = productService.addProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addBlock")
    public ResponseEntity<Block> addBlock(@RequestParam String productId, @RequestParam String data) {
        try {
            Block newBlock = productService.addBlock(productId, data);
            return new ResponseEntity<>(newBlock, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/scanQRCode")
    public ResponseEntity<Product> scanQRCode(@RequestParam String encryptedData) {
        try {
            Product product = productService.processScannedQRCodeData(encryptedData);

            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    


}
