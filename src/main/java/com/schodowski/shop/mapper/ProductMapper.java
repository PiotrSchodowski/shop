package com.schodowski.shop.mapper;

import com.schodowski.shop.dto.ProductDto;
import com.schodowski.shop.repository.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .name(productEntity.getName())
                .category(productEntity.getCategory())
                .price(productEntity.getPrice())
                .build();
    }


    public ProductEntity toEntity(ProductDto productDto) {
        return ProductEntity.builder()
                .name(productDto.getName())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .build();
    }
}
