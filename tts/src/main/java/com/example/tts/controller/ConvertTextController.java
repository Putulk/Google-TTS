package com.example.tts.controller;

import com.example.tts.domain.LanguageConverterCustom;
import com.example.tts.service.ConvertTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/convert-text")
public class ConvertTextController {
    @Autowired
    ConvertTextService convertTextService;

    @GetMapping("")
    public String convertLanguage(@RequestBody LanguageConverterCustom languageConverterCustom) throws IOException {
        return convertTextService.translateText(languageConverterCustom);
    }
}
