package com.felix.DrugTracker.Repository;

import com.felix.DrugTracker.entity.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockchainRepository extends JpaRepository<Blockchain, Long> {
    Blockchain findByProductId(String productId);
}
