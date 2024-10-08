package com.schodowski.shop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private String productName;
    private float quantity;
    private float orderValue;

}
