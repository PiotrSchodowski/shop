package com.schodowski.shop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    String name;
    String category;
}
