package com.example.tts.config;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TtsConfig {
    @Value("${google.application.credentials")
    private String credentialsPath;

    @Bean
    public TextToSpeechClient textToSpeechClient() throws IOException {
        return TextToSpeechClient.create();
    }

}
