package com.felix.DrugTracker.service;

import com.felix.DrugTracker.Repository.BlockchainRepository;
import com.felix.DrugTracker.entity.Block;
import com.felix.DrugTracker.entity.Blockchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlockchainService {

    @Autowired
    private final BlockchainRepository blockchainRepository;
    public static Map<String, List<Block>> blockchains = new HashMap<>();
    public static int difficulty = 5;

    public BlockchainService(BlockchainRepository blockchainRepository) {
        this.blockchainRepository = blockchainRepository;
    }

    // Method to get the blockchain for a product ID
    public List<Block> getBlockchain(String productId) {
        Blockchain blockchain = blockchainRepository.findByProductId(productId);
        System.out.println("Component block chain is " +  blockchain);
        return blockchain != null ? blockchain.getBlocks() : null;
    }

    // Method to add a new block to the blockchain
    @Transactional
    public void addBlock(String productId, Block block) {
        try {
            Blockchain blockchain = blockchainRepository.findByProductId(productId);
            if (blockchain == null) {
                blockchain = new Blockchain(productId);
            }

            blockchain.addBlock(block);
            blockchainRepository.save(blockchain); // Save blockchain and cascade to blocks

        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }
    }

    


}
