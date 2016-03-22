package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.AnswerNotFoundException;

import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.CardStatistics;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/statement")
public class StatementController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;
    
    private StudentDetails verifyStudent(int id) throws StudentNotFoundException
    {
        StudentDetails studentDetails = studentService.findStudentDetailsById(id);
        if(studentDetails == null)
            throw new StudentNotFoundException(id);

        return studentDetails;
    }
    
    private Question verifyQuestion(int id) throws QuestionNotFoundException
    {
        Question que = cardService.findQuestionById(id);
        if(que == null)
            throw new QuestionNotFoundException(id);
        return que;
    }

    private Answer verifyAnswer(int id) throws AnswerNotFoundException
    {
        Answer ans = cardService.findAnswerById(id);
        if(ans == null)
            throw new AnswerNotFoundException(id);
        return ans;
    }

    private AbstractStatement verifyStatement(int id) {
        return null;
    }

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<?> entity_counts()
    {
        LOG.info("Retrieving counts of all statements");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        return new ResponseEntity<>(cardStatistics,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public AbstractStatement retrieveStatement(@PathVariable("id") int id)
    {
        LOG.info("Retreiving statement " + id);
        return verifyStatement(id);
    }

    @RequestMapping(value="/questions/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public Question retrieveQuestion(@PathVariable("id") int id)
    {
        LOG.info("Retreiving question " + id);
        return verifyQuestion(id);
    }
    
    @RequestMapping(value="/questions", method = RequestMethod.POST)
    public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question)
    {
        Question que = cardService.createQuestion(question);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuestionUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(que.getId())
                .toUri();

        responseHeaders.setLocation(newQuestionUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }

    @RequestMapping(value="/answers/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public Answer retrieveAnswer(@PathVariable("id") int id)
    {
        LOG.info("Retreiving answer " + id);
        return verifyAnswer(id);
    }

    @RequestMapping(value="/answers", method = RequestMethod.POST)
    public ResponseEntity<?> createAnswer(@Valid @RequestBody Answer answer)
    {
        Answer ans = cardService.createAnswer(answer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newAnswerUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ans.getId())
                .toUri();

        responseHeaders.setLocation(newAnswerUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }

    @RequestMapping(value="/author/{id}")
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public List<AbstractStatement> retrieveStudentStatements(@PathVariable("id") int id)
    {
        StudentDetails studentDetails = verifyStudent(id);

        LOG.info("Retreiving statements by " + id);

        return studentService.findPublishedStatementsBy(studentDetails);
    }



}
