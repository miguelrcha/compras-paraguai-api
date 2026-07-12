package com.miguelrcha.scraping_paraguai.controller;

import com.miguelrcha.scraping_paraguai.dto.ProductDTO;
import com.miguelrcha.scraping_paraguai.enums.CurrencyType;
import com.miguelrcha.scraping_paraguai.scheduler.ProductScheduler;
import com.miguelrcha.scraping_paraguai.service.ProductService;
import com.miguelrcha.scraping_paraguai.service.notification.TelegramNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints para busca de produtos e disparo manual de rotinas de monitoramento/notificação.
 */
@Tag(name = "Produtos", description = "Busca de produtos e rotinas de monitoramento/notificação")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductScheduler productScheduler;
    private final TelegramNotificationService telegramNotificationService;

    @Operation(
            summary = "Buscar produtos",
            description = "Busca produtos por nome na loja comprasparaguai.com.br, filtra os que custam "
                    + "acima de 100 (na moeda escolhida) e ordena por preço crescente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Busca realizada com sucesso (pode retornar lista vazia)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Falha ao acessar/raspar a loja de origem", content = @Content)
    })
    @GetMapping
    public List<ProductDTO> searchProduct(

            @Parameter(description = "Nome ou termo de busca do produto", required = true, example = "iphone 17")
            @RequestParam String name,

            @Parameter(description = "Número máximo de resultados retornados")
            @RequestParam(defaultValue = "10")
            Integer limit,

            @Parameter(description = "Moeda usada para filtrar e ordenar os preços")
            @RequestParam(defaultValue = "USD")
            CurrencyType currency
    ) {

        return productService.searchProduct(
                name,
                limit,
                currency
        );
    }

    @Operation(
            summary = "Disparar monitoramento manualmente",
            description = "Executa de forma síncrona a mesma rotina do agendamento diário: busca a lista de "
                    + "produtos configurados e envia um resumo por Telegram."
    )
    @ApiResponse(responseCode = "200", description = "Monitoramento executado com sucesso",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Scraping executado!")))
    @GetMapping("/monitor")
    public String monitorProducts() {

        productScheduler.monitorProducts();

        return "Scraping executado!";
    }

    @Operation(
            summary = "Testar integração com o Telegram",
            description = "Envia uma mensagem fixa de teste ao chat configurado, para validar o bot do Telegram."
    )
    @ApiResponse(responseCode = "200", description = "Mensagem enviada com sucesso",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Mensagem enviada!")))
    @GetMapping("/telegram-test")
    public String telegramTest() {

        telegramNotificationService.sendMessage(
                "Bot funcionando!"
        );

        return "Mensagem enviada!";
    }
}
