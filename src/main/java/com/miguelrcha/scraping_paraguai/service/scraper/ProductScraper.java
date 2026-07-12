package com.miguelrcha.scraping_paraguai.service.scraper;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;

import java.util.List;

/**
 * Contrato para implementações de scraping de produtos em lojas de terceiros.
 */
public interface ProductScraper {

    /**
     * Busca produtos por nome/termo diretamente na loja de origem.
     *
     * @param productName termo de busca do produto
     * @return produtos encontrados, sem filtro ou ordenação aplicados
     */
    List<ProductDTO> search(String productName);
}