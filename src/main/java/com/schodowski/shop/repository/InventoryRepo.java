package com.schodowski.shop.repository;

import com.schodowski.shop.repository.entity.InventoryEntity;
import com.schodowski.shop.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProduct(ProductEntity productEntity);

    Optional<InventoryEntity> findByProductName(String name);
}
