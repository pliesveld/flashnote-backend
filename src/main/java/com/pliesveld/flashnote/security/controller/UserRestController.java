package com.pliesveld.flashnote.security.controller;

import com.pliesveld.flashnote.security.CurrentUser;
import com.pliesveld.flashnote.security.StudentPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public StudentPrincipal getAuthenticatedUser(@CurrentUser StudentPrincipal studentPrincipal) {
        return studentPrincipal;
    }

//    private static final Logger LOG = LogManager.getLogger();
//
//    @Value("${jwt.header}")
//    private String tokenHeader;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;

//    @RequestMapping(value = "user", method = RequestMethod.GET)
//    public StudentPrincipal getAuthenticatedUser(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader);
//
//        if(token == null)
//            return null;
//
//        LOG.debug(Markers.SECURITY_AUTH, "Auth Token Header: {}", token);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        StudentPrincipal user = (StudentPrincipal) userDetailsService.loadUserByUsername(username);
//        return user;
//    }


}
