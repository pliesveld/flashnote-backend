package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.security.CurrentUser;
import com.pliesveld.flashnote.security.StudentPrincipal;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class AuthTestController {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;


    @RequestMapping(value="/mine", method = RequestMethod.GET)
    @ResponseBody
    public List<Deck> retrieve_mydecks(@CurrentUser StudentPrincipal studentDetails)
    {
        LOG.info("UserDetails " + studentDetails);
        List<Deck> decks = studentService.findDecksByOwner(studentDetails.getId());
        return decks;
    }


    @RequestMapping(value="/byAuth", method = RequestMethod.GET)
    @ResponseBody
    public StudentDTO listDecks(@CurrentUser Student student)
    {
        LOG.info("Student " + student);
        return StudentDTO.convert(student);
    }

    @RequestMapping(value="/byAuth2", method = RequestMethod.GET)
    @ResponseBody
    public StudentDTO listDecks2(@AuthenticationPrincipal StudentPrincipal studentPrincipal)
    {
        LOG.info("StudentPrincipal " + studentPrincipal);
        return StudentDTO.convert(studentPrincipal);
    }

    @RequestMapping(value="/byAuth3", method = RequestMethod.GET)
    @ResponseBody
    public User listDecks3(@AuthenticationPrincipal User user)
    {
        LOG.info("User " + user);
        return user;
    }

    @RequestMapping(value="/byAuth4", method = RequestMethod.GET)
    @ResponseBody
    public UserDetails listDecks3(@AuthenticationPrincipal UserDetails userDetails)
    {
        LOG.info("UserDetails " + userDetails);
        return userDetails;
    }

}
