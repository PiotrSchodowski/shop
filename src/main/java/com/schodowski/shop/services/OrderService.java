package com.schodowski.shop.services;

import com.schodowski.shop.dto.OrderDto;
import com.schodowski.shop.repository.CashRepo;
import com.schodowski.shop.repository.InventoryRepo;
import com.schodowski.shop.repository.OrderRepo;
import com.schodowski.shop.repository.ProductRepo;
import com.schodowski.shop.repository.entity.CashEntity;
import com.schodowski.shop.repository.entity.InventoryEntity;
import com.schodowski.shop.repository.entity.OrderEntity;
import com.schodowski.shop.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CashRepo cashRepo;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public float entryOrder(OrderDto orderDto) {
        logger.info("Processing new order for product: {}", orderDto.getProductName());

        InventoryEntity inventoryEntity = getInventoryEntity(orderDto.getProductName());

        if (orderDto.getQuantity() > inventoryEntity.getQuantity()) {
            logger.warn("Insufficient stock for product: {}", orderDto.getProductName());
            throw new IllegalArgumentException("No enough amount");
        }

        decreaseStockStatus(inventoryEntity, orderDto.getQuantity());
        float orderValue = createOrder(orderDto);
        return updateCashBalance(orderValue);
    }

    private InventoryEntity getInventoryEntity(String productName) {
        return inventoryRepo.findByProductName(productName)
                .orElseThrow(() -> {
                    logger.warn("No inventory found for product: {}", productName);
                    return new IllegalArgumentException("No inventory found");
                });
    }

    private float createOrder(OrderDto orderDto) {
        ProductEntity productEntity = getProductFromDatabase(orderDto.getProductName());

        OrderEntity orderEntity = OrderEntity.builder()
                .date(LocalDateTime.now())
                .product(productEntity)
                .totalOrderValue(productEntity.getPrice() * orderDto.getQuantity())
                .build();

        orderRepo.save(orderEntity);
        logger.info("New order created for product: {}, value: {}", orderDto.getProductName(), orderEntity.getTotalOrderValue());

        return orderEntity.getTotalOrderValue();
    }

    private float updateCashBalance(float orderValue) {
        CashEntity cashEntity = CashEntity.builder()
                .transactionDate(LocalDateTime.now())
                .value(orderValue)
                .build();

        cashRepo.save(cashEntity);
        logger.info("Cash balance updated by: {}", orderValue);

        return orderValue;
    }

    private void decreaseStockStatus(InventoryEntity inventoryEntity, float quantity) {
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() - quantity);
        inventoryRepo.save(inventoryEntity);
        logger.info("Stock updated for product: {}, reduced by: {}", inventoryEntity.getProduct().getName(), quantity);
    }

    private ProductEntity getProductFromDatabase(String productName) {
        return productRepo.findByName(productName)
                .orElseThrow(() -> {
                    logger.warn("Product not found: {}", productName);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found ");
                });
    }
}
