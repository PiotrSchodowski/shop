package com.schodowski.shop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private String name;
    private String category;
    private float price;
}
