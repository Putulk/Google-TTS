package com.example.tts.controller;

import com.example.tts.domain.TtsCustom;
import com.example.tts.service.TtsService;
import com.google.cloud.texttospeech.v1.Voice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class TtsController {

    @Autowired
    private TtsService ttsService;

    @GetMapping("/tts")
    public String covertTextToSpeech(@RequestBody TtsCustom ttsCustom) throws IOException {
        return ttsService.covertTextToSpeech(ttsCustom);
    }

    @GetMapping("/voiceList")
    public List<Voice> listOfVoice(){
        return ttsService.listVoices();
    }

}
