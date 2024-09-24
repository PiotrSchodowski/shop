package com.schodowski.shop.restController;

import com.schodowski.shop.dto.ProductDto;
import com.schodowski.shop.mapper.ProductMapper;
import com.schodowski.shop.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class inventoryController {

    private final InventoryService inventoryService;
    private final ProductMapper productMapper;

    @PostMapping("/api/product/add")
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        return inventoryService.addProduct(productDto);
    }


    @DeleteMapping("/api/product/delete")
    public boolean deleteProduct(@RequestParam long id) {
        return inventoryService.deleteProduct(id);
    }


    @GetMapping("/api/product/getAll")
    public List<ProductDto> getAll() {
        return inventoryService.getAllProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    //dodaj do magazynu jakas ilosc produktu

    //usun z magazynu jakas ilosc produktu


    // wprowadz cene zakupu produktu ( kazdy wprowadzony produkt na magazyn to ososbna pozycja )
}
