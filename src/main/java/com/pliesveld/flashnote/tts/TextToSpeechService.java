package com.pliesveld.flashnote.tts;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@Validated
@Transactional(readOnly = true)
public interface TextToSpeechService {
    byte[] synthesizeText(String message) throws IOException, InterruptedException;
}
