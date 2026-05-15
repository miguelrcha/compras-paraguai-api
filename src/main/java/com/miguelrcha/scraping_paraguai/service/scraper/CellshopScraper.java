package com.miguelrcha.scraping_paraguai.service.scraper;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CellshopScraper implements ProductScraper {

    private static final String BASE_URL =
            "https://www.comprasparaguai.com.br/busca/?q=";

    @Override
    public List<ProductDTO> search(String productName) {

        List<ProductDTO> products = new ArrayList<>();

        try {

            String url = BASE_URL + productName;

            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements items = document.select(
                    ".promocao-produtos-item"
            );

            for (Element item : items) {

                String name = item.select(
                        ".promocao-item-nome a"
                ).text();

                String usdPrice = item.select(
                        ".price-model span"
                ).text();

                String brlPrice = item.select(
                        ".promocao-item-preco-text"
                ).text();

                products.add(
                        ProductDTO.builder()
                                .name(name)
                                .priceUsd(parsePrice(usdPrice))
                                .priceBrl(parsePrice(brlPrice))
                                .store("Compras Paraguai")
                                .stock(true)
                                .build()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao buscar produtos", e
            );
        }

        return products;
    }

    private Double parsePrice(String price) {

        if (price == null || price.isBlank()) {
            return 0.0;
        }

        String cleanPrice = price
                .replace("US$", "")
                .replace("R$", "")
                .replace("\u00A0", "")
                .replace(".", "")
                .replace(",", ".")
                .replace("\n", "")
                .trim();

        System.out.println("Preço limpo: " + cleanPrice);

        return Double.parseDouble(cleanPrice);
    }
}