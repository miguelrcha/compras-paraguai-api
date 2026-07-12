package com.miguelrcha.scraping_paraguai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadados da documentação OpenAPI/Swagger da API.
 * Expõe a spec em {@code /v3/api-docs} e a UI em {@code /swagger-ui.html}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scrapingParaguaiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Compras Paraguai API")
                        .description("API de monitoramento e comparação de preços de produtos em lojas do "
                                + "Paraguai (comprasparaguai.com.br), com conversão USD/BRL e alertas via Telegram.")
                        .version("v0.0.1")
                        .contact(new Contact()
                                .name("Miguel Rocha")
                                .url("https://github.com/miguelrcha"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
