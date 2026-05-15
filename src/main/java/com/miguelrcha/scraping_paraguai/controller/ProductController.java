package com.miguelrcha.scraping_paraguai.controller;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/products")
    public List<ProductDTO> searchProducts(@RequestParam String name) {
        return productService.searchProduct(name);
    }
}
