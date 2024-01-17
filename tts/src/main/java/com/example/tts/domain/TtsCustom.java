package com.example.tts.domain;

import lombok.Data;

@Data
public class TtsCustom {
    private String text;
    private String languageCode;
    private String gender;
    private String voiceName;
}
