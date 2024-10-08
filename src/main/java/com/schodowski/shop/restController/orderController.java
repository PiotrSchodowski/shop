package com.schodowski.shop.restController;

import com.schodowski.shop.dto.OrderDto;
import com.schodowski.shop.repository.entity.OrderEntity;
import com.schodowski.shop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class orderController {

    private final OrderService orderService;

    @PostMapping("/api/order/add")
    public OrderEntity addOrder(@RequestBody OrderDto orderDto){
        return orderService.entryOrder(orderDto);
    }
}
