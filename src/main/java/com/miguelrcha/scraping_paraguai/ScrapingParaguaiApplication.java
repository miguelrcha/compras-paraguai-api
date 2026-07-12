package com.miguelrcha.scraping_paraguai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Ponto de entrada da API de monitoramento e comparação de preços de produtos em lojas
 * do Paraguai. Documentação interativa disponível em {@code /swagger-ui.html} e a spec
 * OpenAPI em {@code /v3/api-docs}.
 */
@EnableScheduling
@SpringBootApplication
public class ScrapingParaguaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapingParaguaiApplication.class, args);
	}

}
