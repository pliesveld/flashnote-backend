package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.security.CurrentUser;
import com.pliesveld.flashnote.security.StudentPrincipal;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( { "/test", "/admin", "/user" })
public class AuthTestController {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @RequestMapping(value="/byAuth1", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void customAnnotation(@CurrentUser StudentPrincipal student)
    {
        LOG.info("@CurrentUser StudentPrincipal " + student);
    }

    @RequestMapping(value="/byAuth2", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void authenticationPrincipal(@AuthenticationPrincipal StudentPrincipal studentPrincipal)
    {
        LOG.info("@AuthenticationPrincipal StudentPrincipal " + studentPrincipal);
    }

    @RequestMapping(value="/byAuth3", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void springsecurityUserObj(@AuthenticationPrincipal User user)
    {
        LOG.info("@AuthenticationPrincipal org.springframework.security.core.userdetails.User " + user);
    }

    @RequestMapping(value="/byAuth4", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void springsecurityUserObj(@AuthenticationPrincipal UserDetails userDetails)
    {
        LOG.info("org.springframework.security.core.userdetails.UserDetails " + userDetails);
    }

}
