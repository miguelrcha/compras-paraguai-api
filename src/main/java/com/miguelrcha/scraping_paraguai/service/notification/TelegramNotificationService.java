package com.miguelrcha.scraping_paraguai.service.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Envia mensagens e fotos para um chat do Telegram via Bot API, usando o token e chat ID
 * configurados em {@code telegram.token} / {@code telegram.chat-id}.
 */
@Service
public class TelegramNotificationService {

    @Value("${telegram.token}")
    private String token;

    @Value("${telegram.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate =
            new RestTemplate();

    /**
     * Envia uma mensagem de texto para o chat configurado.
     *
     * @param message texto da mensagem
     */
    public void sendMessage(String message) {

        String url =
                "https://api.telegram.org/bot"
                        + token
                        + "/sendMessage";

        Map<String, Object> body =
                new HashMap<>();

        body.put("chat_id", chatId);
        body.put("text", message);

        restTemplate.postForObject(
                url,
                body,
                String.class
        );
    }

    /**
     * Envia uma foto (por URL) com legenda para o chat configurado.
     *
     * @param imageUrl URL pública da imagem
     * @param caption  legenda da foto
     */
    public void sendPhoto(
            String imageUrl,
            String caption
    ) {

        String url =
                "https://api.telegram.org/bot"
                        + token
                        + "/sendPhoto";

        Map<String, Object> body =
                new HashMap<>();

        body.put("chat_id", chatId);
        body.put("photo", imageUrl);
        body.put("caption", caption);

        restTemplate.postForObject(
                url,
                body,
                String.class
        );
    }
}