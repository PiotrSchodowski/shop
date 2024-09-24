package com.schodowski.shop.services;

import com.schodowski.shop.dto.ProductDto;
import com.schodowski.shop.mapper.ProductMapper;
import com.schodowski.shop.repository.InventoryRepo;
import com.schodowski.shop.repository.ProductRepo;
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
}
