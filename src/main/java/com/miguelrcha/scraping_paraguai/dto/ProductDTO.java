package com.miguelrcha.scraping_paraguai.dto;

import lombok.Builder;

@Builder
public record ProductDTO(
        String name,
        Double price,
        String currency,
        String store,
        Boolean stock
) {
}
