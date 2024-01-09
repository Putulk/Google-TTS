package com.example.tts.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TtsService {
    private final TextToSpeechClient textToSpeechClient;

    public TtsService(TextToSpeechClient textToSpeechClient) {
        this.textToSpeechClient = textToSpeechClient;
    }

    public List<Voice> listVoices() {
        // Call the API to list available voices
        ListVoicesResponse response = textToSpeechClient.listVoices("gu-IN");

        // Create a new list of Voice objects with excluded fields
        List<Voice> voices = new ArrayList<>();
        for (Voice originalVoice : response.getVoicesList()) {
            // Exclude problematic fields during serialization
            Voice modifiedVoice = excludeUnknownFields(originalVoice);
            voices.add(modifiedVoice);
        }

        // Return the list of modified voices
        return voices;
    }

    private Voice excludeUnknownFields(Voice originalVoice) {
        // Create a copy of the Voice object, excluding the problematic field
        Voice.Builder voiceBuilder = originalVoice.toBuilder();
        voiceBuilder.getLanguageCodesList();
        return voiceBuilder.build();
    }

    public String covertTextToSpeech(String text,
                                     String languageCode,
                                     String voiceName) throws IOException {
        // Initialize TextToSpeechClient
//        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
//        String convertedText= translateText(text, sourceLanguage, targetLanguage);

        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

        // Set voice parameters
        VoiceSelectionParams voice =
                VoiceSelectionParams.newBuilder()
                        .setLanguageCode(languageCode)
                        .setName(voiceName)
                        .setSsmlGender(SsmlVoiceGender.FEMALE)
                        .build();

        // Set audio configuration
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();


        // Perform the text-to-speech request
        SynthesizeSpeechResponse response =
                textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

        // Save the audio content to a file
        byte[] audioContents = response.getAudioContent().toByteArray();
        try (FileOutputStream out = new FileOutputStream("/Users/gupta/Downloads/TTS-Output/bangla-male-2.mp3")) {
            out.write(audioContents);
            System.out.println("Audio content written to file TTS Output folder");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Speech Generated Successfully...";
    }


//    public byte[] covertTextToSpeech(String text, String languageCode, String voiceName) {
//        // Set the text input
//        SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
//
//        // Set voice parameters
//        VoiceSelectionParams voice =
//                VoiceSelectionParams.newBuilder()
//                        .setLanguageCode(languageCode)
//                        .setName(voiceName)
//                        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
//                        .build();
//
//        // Set audio configuration
//        AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build();
//
//        // Perform the text-to-speech request
//        SynthesizeSpeechResponse response =
//                textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
//
//        // Return the audio content
//        return response.getAudioContent().toByteArray();
//    }


    public String translateText(String text, String sourceLanguage, String targetLanguage) throws IOException {
        // Load default credentials
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();

        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLanguage),
                Translate.TranslateOption.targetLanguage(targetLanguage)
        );

        return translation.getTranslatedText();
    }

}
