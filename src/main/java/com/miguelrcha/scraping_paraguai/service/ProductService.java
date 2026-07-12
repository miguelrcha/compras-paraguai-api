package com.miguelrcha.scraping_paraguai.service;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.enums.CurrencyType;
import com.miguelrcha.scraping_paraguai.service.scraper.CellshopScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Aplica regras de negócio sobre os resultados brutos do scraper: filtra produtos
 * abaixo de um preço mínimo, ordena por preço crescente e limita a quantidade de resultados.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final CellshopScraper cellshopScraper;

    /**
     * Busca produtos pelo nome informado, filtra os com preço (na moeda escolhida) acima
     * de 100 e retorna ordenados por preço crescente, limitados à quantidade pedida.
     *
     * @param name     termo de busca do produto
     * @param limit    número máximo de resultados retornados
     * @param currency moeda usada para filtrar e ordenar os preços
     * @return lista de produtos encontrados, já filtrada, ordenada e limitada
     */
    public List<ProductDTO> searchProduct(
            String name,
            Integer limit,
            CurrencyType currency
    ) {

        return cellshopScraper.search(name)
                .stream()


                .filter(product -> {

                    Double price = currency == CurrencyType.BRL
                            ? product.getPriceBrl()
                            : product.getPriceUsd();

                    return price != null && price > 100;
                })


                .sorted((p1, p2) -> {

                    Double price1 = currency == CurrencyType.BRL
                            ? p1.getPriceBrl()
                            : p1.getPriceUsd();

                    Double price2 = currency == CurrencyType.BRL
                            ? p2.getPriceBrl()
                            : p2.getPriceUsd();

                    return price1.compareTo(price2);
                })

                .limit(limit)
                .toList();
    }
}