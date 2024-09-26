package com.schodowski.shop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryDto {

    String name;
    float quantity;
}
