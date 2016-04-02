package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.AnswerNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.StatementNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.CardStatistics;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @RequestMapping(value="/statements/count", method = RequestMethod.GET)
    public ResponseEntity<?> entity_counts()
    {
        LOG.info("Retrieving counts of all statements");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        return new ResponseEntity<>(cardStatistics,HttpStatus.OK);
    }

    @RequestMapping(value="/statements/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value="/answers/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public Answer retrieveAnswer(@PathVariable("id") int id)
    {
        LOG.info("Retreiving answer " + id);
        return verifyAnswer(id);
    }




}
