package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.AnswerNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.StatementNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.request.UpdateQuestionBankRequestJson;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/construct")
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

    private AbstractStatement verifyStatement(int id) throws StatementNotFoundException
    {
        AbstractStatement statement = cardService.findStatementById(id);
        if(statement == null)
            throw new StatementNotFoundException(id);
        return statement;
    }

    @RequestMapping(value="/questionbank", method = RequestMethod.OPTIONS)
    public ResponseEntity methodsAllowed()
    {
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/questionbank", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> retrieveAllQuestionBanks()
    {
        List<QuestionBank> questionBanks = new ArrayList<>();
        QuestionBank questionBank = new QuestionBank();
        questionBank.setDescription("Commonly asked web developer questions.");
        questionBank.setId(5);
        questionBank.add(new Question("What is a RESTful web service?"));
        questionBank.add(new Question("What methods are supported by the HTTP specification?"));
        questionBank.add(new Question("What is a Cross-Origin-Request?"));
        questionBank.add(new Question("Give an example of a stateless request."));
        questionBanks.add(questionBank);

        questionBank = new QuestionBank();
        questionBank.setDescription("Object Oriented Programming");
        questionBank.add(new Question("What is encapsulation?"));
        questionBank.add(new Question("What is composition?"));
        questionBank.add(new Question("What is inheritence?"));
        questionBank.add(new Question("What is abstraction?"));
        questionBanks.add(questionBank);

        return ResponseEntity.ok(questionBanks);
    }

    @RequestMapping(value="/questionbank", method = RequestMethod.POST)
    public ResponseEntity<?> createQuestionBank()
    {
        int id = 5;

        URI newUri = MvcUriComponentsBuilder
                .fromController(StatementController.class)
                .path("/questionbank/{id}")
                .buildAndExpand(id)
                .toUri();


        return ResponseEntity.ok()
                .location(newUri)
                .build();
    }

    @RequestMapping(value = "/questionbank/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> retrieveQuestionBank(@PathVariable("id") int id)
    {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setId(id);

        return ResponseEntity.ok(questionBank);
    }

    @RequestMapping(value = "/questionbank/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteQuestionBank(@PathVariable("id") int id)
    {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/questionbank/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateQuestionBank(@PathVariable("id") int id, @Valid @RequestBody UpdateQuestionBankRequestJson requestJson)
    {
        return ResponseEntity.ok().build();
    }



}
