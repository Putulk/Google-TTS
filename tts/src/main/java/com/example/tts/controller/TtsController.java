package com.example.tts.controller;

import com.example.tts.service.TtsService;
import com.google.cloud.texttospeech.v1.Voice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TtsController {

    @Autowired
    private TtsService ttsService;

    @GetMapping("/tts")
    public String covertTextToSpeech(@RequestPart String text,
                                     @RequestPart String languageCode,
                                     @RequestPart String voice) throws IOException {
        return ttsService.covertTextToSpeech(text, languageCode, voice);
    }

    @GetMapping("/voiceList")
    public List<Voice> listOfVoice(){
        return ttsService.listVoices();
    }

    @GetMapping("/convert_lang")
    public String convertLanguage(@RequestPart String text, @RequestPart String sourceLanguage, @RequestPart String targetLanguage) throws IOException {
        return ttsService.translateText(text, sourceLanguage, targetLanguage);
    }
}
