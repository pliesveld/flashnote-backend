package com.pliesveld.flashnote.validator;


import com.pliesveld.flashnote.exception.AudioFormatNotSupportedException;
import com.pliesveld.flashnote.web.validator.AudioMetadataReader;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static com.pliesveld.flashnote.logging.Markers.SERVICE_ATTACHMENT;


public class AudioMetadataReaderTest {

    private static final Logger LOG = LogManager.getLogger();

    public AudioMetadataReaderTest() {}

    ClassPathResource resource = new ClassPathResource("/scripts/tests/test-data/audio/");
    private static String[] AUDIO = { "sample.wav", "sample.mp3", "sample.ogg", "sample.m4a","iphone.m4a","sample.aiff","mobile.wav" };


    @Test
    public void AudioMetadataReaderTest() throws IOException {
        URL baseURL = null;

        baseURL = resource.getURL();
        LOG.debug(SERVICE_ATTACHMENT,"Could not open file " + baseURL);
        LOG.debug(SERVICE_ATTACHMENT,"Checking for test resources in {}",baseURL);

        for (String audio : AUDIO)
        {
            Resource audioResource = resource.createRelative(audio);

            File file = null;

            file = audioResource.getFile();

            if (!file.exists())
            {
                LOG.warn("File {0} does not exist.",file.getAbsoluteFile().getAbsolutePath());
                continue;
            }

            LOG.debug(SERVICE_ATTACHMENT,"Opening " + file.getName());

            try {
                AudioMetadataReader.readAudioMetadata(file, null);
            } catch (AudioFormatNotSupportedException afnse) {
                LOG.error("reader could not open {}",file.getName());
            }


        }

    }


    @Test
    public void audioContentByContentTest() throws IOException {
        URL baseURL = null;
        baseURL = resource.getURL();
        LOG.debug(SERVICE_ATTACHMENT,"Could not open file " + baseURL);

        LOG.debug(SERVICE_ATTACHMENT,"Checking for test resources in {}",baseURL);

        for (String audio : AUDIO)
        {
            Resource audioResource = resource.createRelative(audio);

            File file = null;

            file = audioResource.getFile();

            if (!file.exists())
            {
                LOG.warn("File {0} does not exist.",file.getAbsoluteFile().getAbsolutePath());
                continue;
            }

            LOG.debug(SERVICE_ATTACHMENT,"Opening " + file.getName());

            try {
                byte[] contents = IOUtils.toByteArray(new FileInputStream(file));
                AudioMetadataReader.readAudioMetadata(file.getName(), contents, null);
            } catch (Exception e) {
                LOG.error("Could not read file.");
            }


        }
    }


/*
    @Test
    public void testMP4() throws IOException, UnsupportedAudioFileException {
        Resource sample = resource.createRelative("sample.m4a");
        AACAudioFileReader aacAudioFileReader = new AACAudioFileReader();
        AudioFileFormat audioFileFormat = aacAudioFileReader.getAudioFileFormat(sample.getFile());
        LOG.debug(SERVICE_ATTACHMENT,audioFileFormat);
    }

    @Test
    public void testAAC() throws IOException, UnsupportedAudioFileException {
        Resource sample = resource.createRelative("iphone.m4a");
        AACAudioFileReader aacAudioFileReader = new AACAudioFileReader();
        AudioFileFormat audioFileFormat = aacAudioFileReader.getAudioFileFormat(sample.getFile());
        LOG.debug(SERVICE_ATTACHMENT,audioFileFormat);
    }
*/

}

