package com.miguelrcha.scraping_paraguai.service;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.service.scraper.CellshopScraper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CellshopScraper cellshopScraper;

    public List<ProductDTO> searchProduct(String name) {
        return cellshopScraper.search(name);
    }
}
