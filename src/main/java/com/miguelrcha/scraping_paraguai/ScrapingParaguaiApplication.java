package com.miguelrcha.scraping_paraguai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ScrapingParaguaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapingParaguaiApplication.class, args);
	}

}
