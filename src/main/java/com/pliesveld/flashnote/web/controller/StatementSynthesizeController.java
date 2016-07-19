package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.tts.TextToSpeechService;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;

@RestController
@RequestMapping(path = "/speech")
public class StatementSynthesizeController {

    @Autowired
    private CardService cardService;

    @Autowired
    private TextToSpeechService speechService;

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> synthesizeQuestion(@PathVariable("id") int id) throws Exception {

        Question question = cardService.findQuestionById(id);

        if (question == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, "audio/mp3");

        String message = question.getContent();
        byte[] message_mp3 = speechService.synthesizeText(message);

        return new ResponseEntity<>(message_mp3, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/answer/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> synthesizeAnswer(@PathVariable("id") int id) throws Exception {

        Answer answer = cardService.findAnswerById(id);

        if (answer == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, "audio/mp3");

        String message = answer.getContent();
        byte[] message_mp3 = speechService.synthesizeText(message);

        return new ResponseEntity<>(message_mp3, responseHeaders, HttpStatus.OK);
    }
}
