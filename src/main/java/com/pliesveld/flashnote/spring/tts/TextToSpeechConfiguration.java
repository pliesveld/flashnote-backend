package com.pliesveld.flashnote.spring.tts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flashnote.tts")
public class TextToSpeechConfiguration {
    private String synthBinary;
    private String transcodeBinary;
    private String wordsPerMinute;
    private String wordGap;
    private String voice;
    private String amplitude;
    private String pitch;
    private boolean logError;

    public TextToSpeechConfiguration() {}

    public String getSynthBinary() { return synthBinary; }

    public void setSynthBinary(String synthBinary) { this.synthBinary = synthBinary; }

    public String getTranscodeBinary() { return transcodeBinary; }

    public void setTranscodeBinary(String transcodeBinary) { this.transcodeBinary = transcodeBinary; }

    public String getWordsPerMinute() { return wordsPerMinute; }

    public void setWordsPerMinute(String wordsPerMinute) { this.wordsPerMinute = wordsPerMinute; }

    public String getWordGap() { return wordGap; }

    public void setWordGap(String wordGap) { this.wordGap = wordGap; }

    public String getVoice() { return voice; }

    public void setVoice(String voice) { this.voice = voice; }

    public String getAmplitude() { return amplitude; }

    public void setAmplitude(String amplitude) { this.amplitude = amplitude; }

    public String getPitch() { return pitch; }

    public void setPitch(String pitch) { this.pitch = pitch; }

    public boolean isLogError() { return logError; }

    public void setLogError(boolean logError) { this.logError = logError; }
}
