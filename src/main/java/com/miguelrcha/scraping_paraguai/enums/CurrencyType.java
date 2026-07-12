package com.miguelrcha.scraping_paraguai.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Moedas suportadas para filtrar e ordenar os preços dos produtos buscados.
 */
@Schema(description = "Moeda usada para filtrar e ordenar os preços")
public enum CurrencyType {
    USD,
    BRL
}
