package com.pliesveld.flashnote.security.controller;

import com.pliesveld.flashnote.model.json.request.JwtAuthenticationRequestJson;
import com.pliesveld.flashnote.model.json.response.JwtAuthenticationResponseJson;
import com.pliesveld.flashnote.security.JwtTokenUtil;
import com.pliesveld.flashnote.security.StudentPrincipal;
import com.pliesveld.flashnote.spring.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.pliesveld.flashnote.logging.Markers.SECURITY_AUTH;

@RestController
@Profile(value = Profiles.AUTH)
public class AuthenticationRestController {
    private static final Logger LOG = LogManager.getLogger();

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/auth", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> discoverAuthEndpoint() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequestJson authenticationRequest, HttpServletRequest httpRequest) throws AuthenticationException {

        LOG.debug(SECURITY_AUTH, "Authenticating {} / {}", authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UsernamePasswordAuthenticationToken auth_token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        auth_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(auth_token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponseJson(token));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        StudentPrincipal user = (StudentPrincipal) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponseJson(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
