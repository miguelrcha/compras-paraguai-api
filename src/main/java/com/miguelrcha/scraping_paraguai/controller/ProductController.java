package com.miguelrcha.scraping_paraguai.controller;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.enums.CurrencyType;
import com.miguelrcha.scraping_paraguai.scheduler.ProductScheduler;
import com.miguelrcha.scraping_paraguai.service.ProductService;
import com.miguelrcha.scraping_paraguai.service.notification.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductScheduler productScheduler;
    private final TelegramNotificationService telegramNotificationService;

    @GetMapping
    public List<ProductDTO> searchProduct(

            @RequestParam String name,

            @RequestParam(defaultValue = "10")
            Integer limit,

            @RequestParam(defaultValue = "USD")
            CurrencyType currency
    ) {

        return productService.searchProduct(
                name,
                limit,
                currency
        );
    }

    @GetMapping("/monitor")
    public String monitorProducts() {

        productScheduler.monitorProducts();

        return "Scraping executado!";
    }

    @GetMapping("/telegram-test")
    public String telegramTest() {

        telegramNotificationService.sendMessage(
                "Bot funcionando!"
        );

        return "Mensagem enviada!";
    }
}