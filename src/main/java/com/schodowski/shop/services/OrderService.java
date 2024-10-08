package com.schodowski.shop.services;

import com.schodowski.shop.dto.OrderDto;
import com.schodowski.shop.repository.InventoryRepo;
import com.schodowski.shop.repository.OrderRepo;
import com.schodowski.shop.repository.ProductRepo;
import com.schodowski.shop.repository.entity.InventoryEntity;
import com.schodowski.shop.repository.entity.OrderEntity;
import com.schodowski.shop.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public OrderEntity entryOrder(OrderDto orderDto) {

        Optional<InventoryEntity> optionalInventory = inventoryRepo.findByProductName(orderDto.getProductName());
        if (optionalInventory.isPresent()) {
            InventoryEntity inventoryEntity = optionalInventory.get();
            if (orderDto.getQuantity() < inventoryEntity.getQuantity()) {
                return makeNewOrder(orderDto);
            }
        } else {
            throw new IllegalArgumentException("Brak na stanie");
        }
        return null;
        //jesli jest na stanie to pobierz ze stanów
    }


    private OrderEntity makeNewOrder(OrderDto orderDto) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(LocalDateTime.now());
        orderEntity.setProduct(getProductFromDatabase(orderDto.getProductName()));
        orderEntity.setTotalOrderValue(orderEntity.getProduct().getPrice() * orderDto.getQuantity());
        return orderRepo.save(orderEntity);
    }


    private ProductEntity getProductFromDatabase(String productName) {
        return productRepo.findByName(productName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produkt nie istnieje"));
    }
}
