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

/**
 * Rotina agendada (e disparável manualmente via {@code GET /api/products/monitor}) que
 * busca os produtos configurados em {@link ProductConfig}, monta um resumo de preços
 * e envia via Telegram.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductScheduler {

    private final ProductService productService;
    private final ProductConfig productConfig;
    private final TelegramNotificationService telegramNotificationService;

    /**
     * Executa, para cada produto configurado em {@link ProductConfig}, uma busca (até 30
     * resultados em USD, filtrados por relevância e limitados aos 5 primeiros), monta uma
     * mensagem de resumo e a envia por Telegram como texto e como foto com legenda.
     * Roda todo dia às 9h (horário de São Paulo).
     */
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

    /**
     * Filtra falsos positivos do scraper checando se o nome do produto encontrado condiz
     * com o termo buscado, usando sinônimos manuais para os termos mais ambíguos
     * (ex: "ps5" -> "playstation 5").
     */
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