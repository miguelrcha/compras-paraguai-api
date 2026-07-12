package com.miguelrcha.scraping_paraguai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Representa um produto encontrado numa loja, com preços em USD e BRL.
 */
@Schema(description = "Produto encontrado na loja, com preços em USD e BRL")
@Data
@Builder
public class ProductDTO {

    @Schema(description = "Nome do produto conforme listado na loja", example = "iPhone 17 Pro 256GB")
    private String name;

    @Schema(description = "Preço em dólares americanos", example = "999.0")
    private Double priceUsd;

    @Schema(description = "Preço em reais", example = "5399.0")
    private Double priceBrl;

    @Schema(description = "Moeda de referência do preço (atualmente não preenchida pelo scraper)", example = "USD")
    private String currency;

    @Schema(description = "Nome da loja de origem", example = "Compras Paraguai")
    private String store;

    @Schema(description = "Indica se o produto está em estoque", example = "true")
    private Boolean stock;
}
