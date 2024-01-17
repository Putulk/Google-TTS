package com.example.tts.service;

import com.example.tts.domain.LanguageConverterCustom;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ConvertTextService {

    public String translateText(LanguageConverterCustom languageConverterCustom) throws IOException {
        // Load default credentials
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();

        // Detect language
        Detection detection = translate.detect(languageConverterCustom.getSourceText());
        log.info("Detected Source Language: {}", detection.getLanguage());

        // Translate text
        Translation translation = translate.translate(
                languageConverterCustom.getSourceText(),
                Translate.TranslateOption.sourceLanguage(detection.getLanguage()),
                Translate.TranslateOption.targetLanguage(languageConverterCustom.getTargetLanguage())
        );
        log.info("Target language: {}",languageConverterCustom.getTargetLanguage());
        log.info("Translated Text: {}",translation.getTranslatedText());
        return translation.getTranslatedText();
    }
}
