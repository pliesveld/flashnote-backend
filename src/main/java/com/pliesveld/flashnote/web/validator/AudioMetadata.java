package com.pliesveld.flashnote.web.validator;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

public class AudioMetadata {
    private final AudioFileFormat audioFileFormat;
    private final AudioFormat audioFormat;
    private final String mimeContentType;

    public AudioMetadata(AudioFileFormat audioFileFormat, AudioFormat audioFormat, String mimeContentType) {
        if(audioFileFormat == null && audioFormat == null)
            throw new NullPointerException("Cannot create AudioMedata both null arguments");
        this.audioFileFormat = audioFileFormat;
        this.audioFormat = audioFormat;
        this.mimeContentType = mimeContentType;
    }

    public AudioMetadata(AudioFileFormat audioFileFormat, String mimeContentType) {
        this.audioFileFormat = audioFileFormat;
        this.mimeContentType = mimeContentType;
        audioFormat = null;
    }

    public AudioMetadata(AudioFormat audioFormat, String mimeContentType) {
        this.audioFormat = audioFormat;
        this.mimeContentType = mimeContentType;
        audioFileFormat = null;
    }

    public AudioFileFormat getAudioFileFormat() {
        return audioFileFormat;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public String getMimeContentType() { return mimeContentType; }
}
