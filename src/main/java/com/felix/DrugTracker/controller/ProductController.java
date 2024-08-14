package com.felix.DrugTracker.controller;

import com.felix.DrugTracker.entity.Block;
import com.felix.DrugTracker.entity.Blockchain;
import com.felix.DrugTracker.entity.Product;
import com.felix.DrugTracker.entity.ProductResponse;
import com.felix.DrugTracker.service.BlockchainService;
import com.felix.DrugTracker.service.ProductService;
import com.felix.DrugTracker.util.QRCodeUtil;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BlockchainService blockchainService;

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
    public ResponseEntity<?> scanQRCode(@RequestParam("image") MultipartFile file) {
        System.out.println("In the scan qr code section");
        try (InputStream inputStream = file.getInputStream()){
            // Read the QR code from the image
            String encryptedData = QRCodeUtil.readQRCodeImage(inputStream);

            System.out.println("Got passed the read qr");
            System.out.println(encryptedData);
            // Process the scanned QR code data

            Product product = productService.processScannedQRCodeData(encryptedData);

            if (product == null) {
                // Product not found
                return createErrorResponse("Product not found.", HttpStatus.NOT_FOUND);
            }

            List<Map<String, Object>> blockDetailsList = getBlockDetails(product.getUniqueId());

            ProductResponse response = new ProductResponse(product, blockDetailsList);


            System.out.println(product);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("IO Exception");
            return createErrorResponse("Internal Server Error. Unable to process the image.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            System.out.println("Image has not been found");
            return createErrorResponse("QR code not found in the image.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("General exception thrown");
            return createErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam("query") String productCode) {
        try {

            System.out.println(productCode);
            // Search for the product using the product code
            Product product = productService.findProduct(productCode);

            if (product == null) {
                System.out.println("Product not found in search product");
                // Product not found
                return createErrorResponse("Product not found.", HttpStatus.NOT_FOUND);
            }

            // Retrieve the blockchain details for the found product
            List<Map<String, Object>> blockDetailsList = getBlockDetails(product.getUniqueId());


            // Create the response with product and block details
            ProductResponse response = new ProductResponse(product, blockDetailsList);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Handle general exceptions
            return createErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private List<Map<String, Object>> getBlockDetails(String productId) {
        List<Block> blocks = blockchainService.getBlockchain(productId);
        List<Map<String, Object>> blockDetailsList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Block block : blocks) {
            Map<String, Object> blockDetails = new HashMap<>();
            blockDetails.put("username", block.getUser().getUsername());
            blockDetails.put("role", block.getUser().getRole());
            String formattedDate = block.getTimeStamp().format(formatter);
            blockDetails.put("timeline", formattedDate);
            blockDetailsList.add(blockDetails);
        }

        return blockDetailsList;
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }



}
