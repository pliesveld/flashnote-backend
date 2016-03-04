package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.exception.AudioFormatNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.pliesveld.flashnote.logging.Markers.SERVICE_ATTACHMENT;

public class AudioMetadataReader {
    private static final Logger LOG = LogManager.getLogger();

    public static AudioMetadata readAudioMetadata(InputStream is, String mimeContentType) throws AudioFormatNotSupportedException {
        verifyMimeTypeSupported(mimeContentType);

        try {
            AudioFileFormat format = AudioSystem.getAudioFileFormat(is);
            LOG.debug(SERVICE_ATTACHMENT,"Audio Format: " + format);
            return new AudioMetadata(format, mimeContentType);

        } catch (UnsupportedAudioFileException |IOException e) {
            LOG.debug(SERVICE_ATTACHMENT,"Could not get audio file format from file.");
            throw new AudioFormatNotSupportedException("Could not read file",e);
        }

    }


    public static AudioMetadata readAudioMetadata(File inFile, String mimeContentType) throws AudioFormatNotSupportedException {
        verifyMimeTypeSupported(mimeContentType);
        try {
            AudioFileFormat format = AudioSystem.getAudioFileFormat(inFile);
            LOG.debug(SERVICE_ATTACHMENT,"Format: " + format);
            return new AudioMetadata(format, mimeContentType);
        } catch (UnsupportedAudioFileException|IOException e) {
            LOG.debug(SERVICE_ATTACHMENT,"Could not get audio file format from file.");
            throw new AudioFormatNotSupportedException("Could not read file",e);
        }

    }

    public static AudioMetadata readAudioMetadata(String name, byte[] contents, String mimeContentType) throws AudioFormatNotSupportedException {
        AudioInputStream ais = null;
        verifyMimeTypeSupported(mimeContentType);
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contents);
            ais = AudioSystem.getAudioInputStream(byteArrayInputStream);
            AudioFormat format = ais.getFormat();
            LOG.debug(SERVICE_ATTACHMENT,"audio file format of " + name + ": " + format);
            return new AudioMetadata(format, mimeContentType);
        } catch (IOException e) {
            throw new AudioFormatNotSupportedException("Could not read input stream",e);
        } catch (UnsupportedAudioFileException e) {
            throw new AudioFormatNotSupportedException("No provider supports audio format",e);
        } finally {
            if (ais != null) {
                try {
                    ais.close();
                } catch (IOException e) {
                    throw new AudioFormatNotSupportedException("Could not close audio input stream.",e);
                }
            }
        }
    }

    public static void verifyMimeTypeSupported(String mimeType) throws AudioFormatNotSupportedException {
        //TODO: gather list of mime-types
    }
}

