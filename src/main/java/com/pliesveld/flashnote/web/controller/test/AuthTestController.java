package com.pliesveld.flashnote.web.controller.test;


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

import java.security.Principal;

@RestController
@RequestMapping( { "/anon",  "/auth", "/admin" })
public class AuthTestController {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @RequestMapping(value="/StudentPrincipal", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public StudentPrincipal getAuthByCustomSpringPrincipal(@CurrentUser StudentPrincipal studentPrincipal)
    {
        LOG.info("@CurrentUser StudentPrincipal " + studentPrincipal);
        return studentPrincipal;
    }

    @RequestMapping(value="/User", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getAuthBySpringUser(@AuthenticationPrincipal User user)
    {
        LOG.info("@AuthenticationPrincipal org.springframework.security.core.userdetails.User " + user);
        return user;
    }

    @RequestMapping(value="/UserDetails", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserDetails getAuthByUserDetails(@AuthenticationPrincipal UserDetails userDetails)
    {
        LOG.info("org.springframework.security.core.userdetails.UserDetails " + userDetails);
        return userDetails;
    }

    @RequestMapping(value="/Principal", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Principal getAuthByPrincipal(Principal principal)
    {
        LOG.info("java.security.Principal " + principal);
        return principal;
    }

}
