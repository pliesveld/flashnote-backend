package com.pliesveld.flashnote.spring.tts;

import com.pliesveld.flashnote.tts.TextToSpeechService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {TextToSpeechConfiguration.class, TextToSpeechService.class})
public class TextToSpeechConfig {}
