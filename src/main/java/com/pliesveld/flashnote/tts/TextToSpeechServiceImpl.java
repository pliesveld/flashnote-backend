package com.pliesveld.flashnote.tts;

import com.pliesveld.flashnote.exception.SpeechException;
import com.pliesveld.flashnote.spring.tts.TextToSpeechConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service("textToSpeechService")
public class TextToSpeechServiceImpl implements TextToSpeechService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private TextToSpeechConfiguration speechConfiguration;

    private static final int MAX_MESSAGE_LENGTH = 1024 * 2;

    private static final int MAX_WORD_LENGTH = 12;

    @Override
    public byte[] synthesizeText(final String originalMessage) {

        String message = originalMessage.replaceAll("'s", " is")
                .replaceAll("n't", " not")
                .replaceAll("[\"'(){}<>]", " ")
                .replaceAll("_", "-");

        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new SpeechException("Message is too long.");
        }

        if (!message.matches("[- ,:;.?A-Za-z0-9]+")) {
            throw new SpeechException("Message contained invalid characters.");
        }

        final String[] words = message.split("[.,-?\\d\\s]");
        for (final String word : words) {
            if (word.length() > MAX_WORD_LENGTH) {
                throw new SpeechException("Message contained word longer than " + MAX_WORD_LENGTH + " characters");
            }
        }

        LOG.debug("Synthesizing: " + message);

        String[] proc1_args = {speechConfiguration.getSynthBinary(), "--stdout",
            "-s", speechConfiguration.getWordsPerMinute(), "-g", speechConfiguration.getWordGap(),
            "-v", speechConfiguration.getVoice(), "-p", speechConfiguration.getPitch(),
            "-a", speechConfiguration.getAmplitude(), message};

        ProcessBuilder pb = new ProcessBuilder().command(proc1_args);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);

        if (speechConfiguration.isLogError()) {
            pb.redirectErrorStream(true);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        }

        Process proc = null;
        try {
            proc = pb.start();
        } catch (IOException e) {
            throw new SpeechException("Could not start tts sub-process", e);
        }

        String[] proc2_args = {speechConfiguration.getTranscodeBinary(), "-i", "pipe:0", "-codec:a", "libmp3lame", "-f", "mp3", "-qscale:a", "2", "-q:a", "16k", "-ar", "11025", "pipe:1"};

        ProcessBuilder pb2 = new ProcessBuilder().command(proc2_args);

        if (speechConfiguration.isLogError()) {
            pb2.redirectErrorStream(true);
            pb2.redirectError(ProcessBuilder.Redirect.INHERIT);
        }

        pb2.redirectOutput(ProcessBuilder.Redirect.PIPE);
        pb2.redirectInput(ProcessBuilder.Redirect.PIPE);

        Process proc2 = null;
        try {
            proc2 = pb2.start();
        } catch (IOException e) {
            proc.destroy();
            throw new SpeechException("Could not start conversion sub-process", e);
        }

        InputStream inputStream = new BufferedInputStream(proc.getInputStream());
        OutputStream outputStream = new BufferedOutputStream(proc2.getOutputStream());

        // TODO: try-with-resources

        try {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new SpeechException("Could not pipe data between sub-processes", e);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            throw new SpeechException(e);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            throw new SpeechException(e);
        }

        int ret = 0;
        try {
            ret = proc.waitFor();
        } catch (InterruptedException e) {
            throw new SpeechException(e);
        }

        if (ret != 0) {
            throw new SpeechException("speech synthesizer returned exist status: " + ret);
        }

        int ret2 = 0;
        try {
            ret2 = proc2.waitFor();
        } catch (InterruptedException e) {
            throw new SpeechException(e);
        }
        if (ret2 != 0) {
            throw new SpeechException("audio conversion returned exist status: " + ret2);
        }

        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(new BufferedInputStream(proc2.getInputStream()));
        } catch (IOException e) {
            throw new SpeechException(e);
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/tmp/test.mp3");
        FileOutputStream os = new FileOutputStream(file);
        byte[] contents = new TextToSpeechServiceImpl().synthesizeText("What we got here is a failure to communicate. " +
                "Some men, you just can't reach. " +
                "So you get what we had here last week. " +
                "which is the way he wants it. " +
                "Well, he gets it. " +
                "And I don't like it anymore than you men."

        );
        os.write(contents);
        os.flush();
        os.close();
    }
}
