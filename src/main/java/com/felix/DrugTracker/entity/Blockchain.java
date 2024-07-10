package com.felix.DrugTracker.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Blockchain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    @OneToMany(mappedBy = "blockchain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    public Blockchain() {
    }

    public Blockchain(String productId) {
        this.productId = productId;
    }

    // Getters and setters



    public void addBlock(Block block) {
        blocks.add(block);
        block.setBlockchain(this);  // Maintain bidirectional relationship
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
        block.setBlockchain(null);  // Maintain bidirectional relationship
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

}
