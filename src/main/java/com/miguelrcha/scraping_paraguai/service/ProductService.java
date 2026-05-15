package com.miguelrcha.scraping_paraguai.service;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.enums.CurrencyType;
import com.miguelrcha.scraping_paraguai.service.scraper.CellshopScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CellshopScraper cellshopScraper;

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