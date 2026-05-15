package com.miguelrcha.scraping_paraguai.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
public class ProductConfig {

    private final List<String> products = List.of(
            "iphone 17",
            "macbook",
            "ipad air",
            "playstation 5",
            "memoria ram"
    );
}