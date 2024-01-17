package com.example.tts.domain;

import lombok.Data;

@Data
public class LanguageConverterCustom {
    private String sourceText;
    private String targetText;
    private String sourceLanguage;
    private String targetLanguage;
}
