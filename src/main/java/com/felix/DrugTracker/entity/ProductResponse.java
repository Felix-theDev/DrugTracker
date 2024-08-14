package com.felix.DrugTracker.entity;

import java.util.List;
import java.util.Map;

public class ProductResponse {
    private Product product;
    private List<Map<String, Object>> blockDetails;

    public ProductResponse(Product product, List<Map<String, Object>> blockDetails) {
        this.product = product;
        this.blockDetails = blockDetails;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Map<String, Object>> getBlockDetails() {
        return blockDetails;
    }

    public void setBlockDetails(List<Map<String, Object>> blockDetails) {
        this.blockDetails = blockDetails;
    }
}
