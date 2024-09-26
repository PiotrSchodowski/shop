package com.schodowski.shop.mapper;

import com.schodowski.shop.dto.InventoryDto;
import com.schodowski.shop.repository.entity.InventoryEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {


    public InventoryDto toDto(InventoryEntity inventoryEntity) {
        return InventoryDto.builder()
                .name(inventoryEntity.getProduct().getName())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }

}
