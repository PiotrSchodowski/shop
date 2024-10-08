package com.schodowski.shop.services;

import com.schodowski.shop.dto.InventoryDto;
import com.schodowski.shop.dto.ProductDto;
import com.schodowski.shop.mapper.ProductMapper;
import com.schodowski.shop.repository.InventoryRepo;
import com.schodowski.shop.repository.ProductRepo;
import com.schodowski.shop.repository.entity.InventoryEntity;
import com.schodowski.shop.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);


    public ProductDto addProduct(ProductDto productDto) {

        if(productRepo.findByName(productDto.getName()).isPresent()){
            logger.warn("Product with name {} already exists in the database", productDto.getName());
            throw new IllegalArgumentException("Product already exists in the database");
        }

        logger.info("Adding new product: {}", productDto.getName());
        ProductEntity productEntity = productMapper.toEntity(productDto);
        productRepo.save(productEntity);
        return productMapper.toDto(productEntity);
    }


    public boolean deleteProduct(long id) {
        logger.info("Deleting product with ID: {}", id);

        return productRepo.findById(id)
                .map(product -> {
                    productRepo.deleteById(id);
                    logger.info("Product with ID {} has been deleted", id);
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

        logger.info("Increased quantity of product: {} by {}", inventoryDto.getName(), inventoryDto.getQuantity());
        return mapToInventoryDto(inventoryEntity);
    }


    public boolean decreaseProduct(InventoryDto inventoryDto) {
        ProductEntity productEntity = getProductByName(inventoryDto.getName());
        InventoryEntity inventoryEntity = getInventoryByProduct(productEntity);

        if (inventoryEntity.getQuantity() < inventoryDto.getQuantity()) {
            logger.warn("Attempting to remove more quantity of product {} than available in stock", inventoryDto.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quantity of product in stock");
        }

        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - inventoryDto.getQuantity());
        inventoryRepo.save(inventoryEntity);

        logger.info("Reduced quantity of product: {} by {}", inventoryDto.getName(), inventoryDto.getQuantity());
        return true;
    }


    private ProductEntity getProductByName(String name) {
        return productRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist"));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No inventory record for this product"));
    }


    private InventoryDto mapToInventoryDto(InventoryEntity inventoryEntity) {
        return InventoryDto.builder()
                .name(inventoryEntity.getProduct().getName())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }
}
