package com.miguelrcha.scraping_paraguai.service.scraper;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;

import java.util.List;

public interface ProductScraper {

    List<ProductDTO> search(String productName);
}