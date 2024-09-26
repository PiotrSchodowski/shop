package com.schodowski.shop.services;

import com.schodowski.shop.dto.InventoryDto;
import com.schodowski.shop.dto.ProductDto;
import com.schodowski.shop.mapper.ProductMapper;
import com.schodowski.shop.repository.InventoryRepo;
import com.schodowski.shop.repository.ProductRepo;
import com.schodowski.shop.repository.entity.InventoryEntity;
import com.schodowski.shop.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public ProductDto addProduct(ProductDto productDto) {
        ProductEntity productEntity = productMapper.toEntity(productDto);
        productRepo.save(productEntity);
        return productMapper.toDto(productEntity);
    }


    public boolean deleteProduct(long id) {
        return productRepo.findById(id)
                .map(product -> {
                    productRepo.deleteById(id);
                    return true;
                })
                .orElse(false);
    }


    public List<ProductEntity> getAllProducts() {
        return productRepo.findAll();
    }

    public List<InventoryEntity> getAllInventory() {
        return inventoryRepo.findAll();
    }


    public InventoryDto increaseProduct(InventoryDto inventoryDto) {
        ProductEntity productEntity = getProductByName(inventoryDto.getName());
        InventoryEntity inventoryEntity = getOrCreateInventory(productEntity);

        inventoryEntity.setQuantity(inventoryEntity.getQuantity() + inventoryDto.getQuantity());
        inventoryRepo.save(inventoryEntity);

        return mapToInventoryDto(inventoryEntity);
    }


    public boolean decreaseProduct(InventoryDto inventoryDto) {
        ProductEntity productEntity = getProductByName(inventoryDto.getName());
        InventoryEntity inventoryEntity = getInventoryByProduct(productEntity);

        if (inventoryEntity.getQuantity() < inventoryDto.getQuantity()) {
            throw new IllegalArgumentException("Product quantity on inventory is insufficient");
        }

        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - inventoryDto.getQuantity());
        inventoryRepo.save(inventoryEntity);

        return true;
    }


    private ProductEntity getProductByName(String name) {
        return productRepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }


    private InventoryEntity getOrCreateInventory(ProductEntity productEntity) {
        return inventoryRepo.findByProduct(productEntity)
                .orElse(InventoryEntity.builder()
                        .product(productEntity)
                        .quantity(0)
                        .build());
    }


    private InventoryEntity getInventoryByProduct(ProductEntity productEntity) {
        return inventoryRepo.findByProduct(productEntity)
                .orElseThrow(() -> new IllegalArgumentException("Product does not have inventory record"));
    }


    private InventoryDto mapToInventoryDto(InventoryEntity inventoryEntity) {
        return InventoryDto.builder()
                .name(inventoryEntity.getProduct().getName())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }


}

