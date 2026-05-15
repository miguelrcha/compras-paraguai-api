package com.miguelrcha.scraping_paraguai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

    private String name;
    private Double priceUsd;
    private Double priceBrl;
    private String currency;
    private String store;
    private Boolean stock;
}