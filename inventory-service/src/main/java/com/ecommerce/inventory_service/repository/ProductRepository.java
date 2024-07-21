package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long id);

    Optional<Product> findByProductName(String productName);
}
