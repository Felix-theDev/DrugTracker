package com.felix.DrugTracker.service;

import com.felix.DrugTracker.Repository.ProductRepository;
import com.felix.DrugTracker.entity.Block;
import com.felix.DrugTracker.entity.Product;
import com.felix.DrugTracker.entity.User;
import com.felix.DrugTracker.util.CryptoUtil;
import com.felix.DrugTracker.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

   @Autowired
    ProductRepository productRepository;

   @Autowired
   BlockchainService blockchainService;

   @Autowired
   UserService userService;

    public ProductService(ProductRepository productRepository, BlockchainService blockchainService, UserService userService) {
        this.productRepository = productRepository;
        this.blockchainService = blockchainService;
        this.userService = userService;

    }

    public Product addProduct(Product product) throws Exception {
        System.out.println("In the add product code in service ");

        if (product.getUniqueId() == null || product.getUniqueId().isEmpty()) {
            product.setUniqueId(product.getUniqueId());
        }

        String uniqueId = product.getUniqueId();
        System.out.println("The unique id in service code is " + uniqueId);

        Product savedProduct = null;
        String qrCodeDir = "path/to/qrcodes/";
        Path qrCodeDirPath = Paths.get(qrCodeDir);
        try {
            Files.createDirectories(qrCodeDirPath);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle directory creation failure if necessary
        }

   try {
            String encryptedData = CryptoUtil.encryptToBase64("YourSecretKey", uniqueId);
            System.out.println("The encrypted data is " + encryptedData);
            String qrCodePath = "path/to/qrcodes/" + product.getName() + ".png";
            QRCodeUtil.generateQRCodeImage(encryptedData, 250, 250, qrCodePath);
            product.setQrCode(qrCodePath);
            savedProduct = productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create a genesis block for the new product
        try {
            Block genesisBlock = new Block(product.getName(), "0", getCurrentUser());
//        blockchainService.addBlock(product.getId().toString(), genesisBlock);
            blockchainService.addBlock(product.getUniqueId(), genesisBlock);

        }catch (Exception e){
            e.printStackTrace();
        }

        return savedProduct;
    }


    public Block addBlock(String productId, String data) {
        List<Block> blockchain = blockchainService.getBlockchain(productId);
        String previousHash = blockchain.isEmpty() ? "0" : blockchain.get(blockchain.size() - 1).getHash();
        Block newBlock = new Block(data, previousHash, getCurrentUser());
        blockchainService.addBlock(productId, newBlock);

        return newBlock;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        System.out.println("Username is " + username);
        return userService.findByUsername(username);
    }

    public Product findProduct(String uniqueId){
        Product product = null;
        if(uniqueId != null && !uniqueId.isEmpty()){
            product = productRepository.findByUniqueId(uniqueId);
        }
        return product;
    }
    public Product processScannedQRCodeData(String encryptedData) {
        try {
            // Decrypt the scanned data to get the uniqueId
            String uniqueId = CryptoUtil.decryptFromBase64("YourSecretKey", encryptedData);
            System.out.println("The unique id is " + uniqueId);
            // Check if the uniqueId is valid
            if (uniqueId.trim().isEmpty()) {
                // If the uniqueId is invalid, return null
                return null;
            }

            System.out.println("Unique id is " + uniqueId + " Class is: " + getClass().getName());
            // Use the uniqueId to retrieve product information
            Product product = findProduct(uniqueId);

            // Return the product if found, otherwise return null
            return product;
        } catch (Exception e) {
            // Handle exceptions that occur during decryption
            System.out.println("Error during decryption: " + e.getMessage());
            return null;
        }
    }

}
