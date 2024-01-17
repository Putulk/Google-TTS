package com.example.tts.service;

import com.example.tts.domain.TtsCustom;
import com.example.tts.enums.LanguageCode;
import com.example.tts.enums.VoiceName;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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

    public String covertTextToSpeech(TtsCustom ttsCustom) throws IOException {
        SynthesisInput input = SynthesisInput.newBuilder().setText(ttsCustom.getText()).build();

        // Set voice parameters
        VoiceSelectionParams voice =
                VoiceSelectionParams.newBuilder()
                        .setLanguageCode(getLanguageCode(ttsCustom.getLanguageCode()))
                        .setName(getLanguageCode(ttsCustom.getLanguageCode())+"-"+getVoiceName(ttsCustom.getVoiceName(), LanguageCode.valueOf(ttsCustom.getLanguageCode())))
                        .setSsmlGender(convertToSsmlGender(ttsCustom.getVoiceName()))
                        .build();

        if (ttsCustom.getLanguageCode().equals("LANGUAGE_CODE_ENGLISH") || ttsCustom.getLanguageCode().equals("LANGUAGE_CODE_HINDI")) {
            // Set audio configuration
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build();


            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Save the audio content to a file
            byte[] audioContents = response.getAudioContent().toByteArray();
            try (FileOutputStream out = new FileOutputStream("/Users/gupta/Downloads/TTS-Output/" + ttsCustom.getVoiceName() + "_" + ttsCustom.getLanguageCode() + ".wav")) {
                out.write(audioContents);
                log.info("Audio content written to file TTS Output folder");
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("This is .wav output...");
            return "Speech Generated Successfully...";
        }else {
            // Set audio configuration
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();


            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Save the audio content to a file
            byte[] audioContents = response.getAudioContent().toByteArray();
            try (FileOutputStream out = new FileOutputStream("/Users/gupta/Downloads/TTS-Output/" +
                    ttsCustom.getVoiceName()+"-"+getLanguageCode(ttsCustom.getLanguageCode()) + ".mp3")) {
                out.write(audioContents);
                log.info("Audio content written to file TTS Output folder");
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("This is .mp3 output...");
            return "Speech Generated Successfully...";
        }

    }

    public  SsmlVoiceGender convertToSsmlGender(String voiceName) {
        VoiceName voiceNameEnum= VoiceName.valueOf(voiceName);
        switch (voiceNameEnum) {
            case MALE_VOICE:
                return SsmlVoiceGender.MALE;
            case FEMALE_VOICE:
                return SsmlVoiceGender.FEMALE;
            default:
                throw new IllegalArgumentException("Unsupported custom gender: " + voiceName);
        }
    }

    public String getLanguageCode(String languageCode){
        LanguageCode languageCodeEnum= LanguageCode.valueOf(languageCode);
        // Set userLanguageCode based on user choice
        switch (languageCodeEnum) {
            case LANGUAGE_CODE_HINDI:
                return "hi-IN";
            case LANGUAGE_CODE_ENGLISH:
                return "en-IN";
            case LANGUAGE_CODE_BENGALI:
                return "bn-IN";
            default:
                throw new IllegalArgumentException("Unsupported custom language: " + languageCode);
        }
    }

    public String getVoiceName(String voiceName, LanguageCode languageCode){
        VoiceName voiceNameEnum= VoiceName.valueOf(voiceName);
        switch (voiceNameEnum) {
            case MALE_VOICE:
                if (languageCode == LanguageCode.LANGUAGE_CODE_ENGLISH) {
                    log.info("Neural2-C");
                    return "Neural2-C";
                } else if(languageCode == LanguageCode.LANGUAGE_CODE_HINDI){
                    log.info("Neural2-C");
                    return "Neural2-C";
                }else if (languageCode == LanguageCode.LANGUAGE_CODE_BENGALI){
                    log.info("Standard-B");
                    return "Standard-B";
                }else {
                    return getRandomStandardVoice();
                }
            case FEMALE_VOICE:
                if (languageCode == LanguageCode.LANGUAGE_CODE_ENGLISH) {
                    return "Neural2-A";
                } else if(languageCode == LanguageCode.LANGUAGE_CODE_HINDI){
                    return "Neural2-A";
                }else if (languageCode == LanguageCode.LANGUAGE_CODE_BENGALI){
                    return "Standard-A";
                }else {
                    return getRandomStandardVoice();
                }
            default:
                throw new IllegalArgumentException("Unsupported custom gender: " + voiceName);
        }
    }

    private String getRandomStandardVoice() {
        int randomIndex = (int) (Math.random() * 3);
        switch (randomIndex) {
            case 0:
                return "Standard-B";
            case 1:
                return "Standard-C";
            case 2:
                return "Standard-D";
            default:
                throw new IllegalStateException("Unexpected value: " + randomIndex);
        }
    }

}
