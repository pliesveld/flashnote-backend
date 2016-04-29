package com.pliesveld.flashnote.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.StatementNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.model.json.request.UpdateQuestionBankRequestJson;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping(path = "/questionbanks")
public class QuestionBankController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper jackson2ObjectMapper)
    {
        objectMapper = jackson2ObjectMapper;
        Views.debug(this,objectMapper);
    }
    
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

    private AbstractStatement verifyStatement(int id) throws StatementNotFoundException
    {
        AbstractStatement statement = cardService.findStatementById(id);
        if(statement == null)
            throw new StatementNotFoundException(id);
        return statement;
    }

    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity methodsAllowed()
    {
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> retrieveAllQuestionBanks()
    {
        List<QuestionBank> questionBanks = cardService.findAllQuestionBanks();
        return ResponseEntity.ok(questionBanks);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createQuestionBank(@Valid @RequestBody QuestionBank questionBank)
    {
        Category category = questionBank.getCategory();
        if(category == null || category.getId() == null)
        {
            throw new CategoryNotFoundException(0);
        }

        int id = category.getId();
        if(!cardService.doesCategoryIdExist(id))
        {

            throw new CategoryNotFoundException(id);
        }

        questionBank = cardService.createQuestionBank(questionBank);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                        .fromController(QuestionBankController.class)
                        .path("/{id}")
                        .buildAndExpand(questionBank.getId())
                        .toUri()).build();

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> retrieveQuestionBank(@PathVariable("id") int id) throws JsonProcessingException {
        QuestionBank questionBank = cardService.findQuestionBankById(id);

        if(questionBank == null )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(objectMapper.writeValueAsString(questionBank));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteQuestionBank(@PathVariable("id") int id)
    {
        cardService.deleteQuestionBank(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateQuestionBank(@PathVariable("id") int id, @Valid @RequestBody UpdateQuestionBankRequestJson requestJson)
    {
        EnumMap<UpdateQuestionBankRequestJson.UpdateOperation, Consumer<UpdateQuestionBankRequestJson>> action = null;
        action.get(requestJson.getOperation()).accept(requestJson);
        return ResponseEntity.ok().build();
    }



}
