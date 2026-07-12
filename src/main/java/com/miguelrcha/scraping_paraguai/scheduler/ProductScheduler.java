package com.miguelrcha.scraping_paraguai.scheduler;

import com.miguelrcha.scraping_paraguai.config.ProductConfig;
import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.enums.CurrencyType;
import com.miguelrcha.scraping_paraguai.service.ProductService;
import com.miguelrcha.scraping_paraguai.service.notification.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductScheduler {

    private final ProductService productService;
    private final ProductConfig productConfig;
    private final TelegramNotificationService telegramNotificationService;

    @Scheduled(cron = "0 0 9 * * *", zone = "America/Sao_Paulo")
    public void monitorProducts() {

        log.info("Iniciando monitoramento de produtos...");

        StringBuilder message =
                new StringBuilder();

        message.append("📦 Scraping de Produtos - comprasparaguai.com.br 📦\n\n");

        for (String product : productConfig.getProducts()) {

            List<ProductDTO> results =
                    productService.searchProduct(
                                    product,
                                    30,
                                    CurrencyType.USD
                            )
                            .stream()
                            .filter(item -> isRelevantProduct(
                                    product,
                                    item.getName()
                            ))
                            .limit(5)
                            .toList();

            log.info("Produto monitorado: {}", product);

            message.append("🔎 ")
                    .append(product.toUpperCase())
                    .append("\n");

            results.forEach(item -> {

                log.info("{} - USD {}",
                        item.getName(),
                        item.getPriceUsd()
                );

                message.append("• ")
                        .append(item.getName())
                        .append("\n")
                        .append("💵 USD ")
                        .append(item.getPriceUsd())
                        .append("\n")
                        .append("💰 BRL R$ ")
                        .append(item.getPriceBrl())
                        .append("\n\n");
            });

            message.append("\n");
        }

        telegramNotificationService.sendMessage(
                message.toString()
        );

        log.info("Monitoramento finalizado.");
    }

    private boolean isRelevantProduct(
            String search,
            String productName
    ) {

        String normalizedSearch =
                search.toLowerCase();

        String normalizedName =
                productName.toLowerCase();

        return switch (normalizedSearch) {

            case "ps5" ->
                    normalizedName.contains("playstation 5")
                            || normalizedName.contains("ps5");

            case "iphone" ->
                    normalizedName.contains("iphone");

            case "macbook" ->
                    normalizedName.contains("macbook");

            case "ipad" ->
                    normalizedName.contains("ipad");

            case "memoria ram" ->
                    normalizedName.contains("ram")
                            || normalizedName.contains("ddr");

            default ->
                    normalizedName.contains(
                            normalizedSearch
                    );
        };
    }
}