package com.felix.DrugTracker.Repository;

import com.felix.DrugTracker.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByUniqueId(String uniqueId);

}
