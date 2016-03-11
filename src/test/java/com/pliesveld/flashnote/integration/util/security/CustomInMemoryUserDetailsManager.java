package com.pliesveld.flashnote.integration.util.security;

import com.pliesveld.flashnote.logging.Markers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//@ConditionalOnMissingBean(name = "authService")
//@Component(value = "inMemoryUserDetails")
public class CustomInMemoryUserDetailsManager implements UserDetailsService {
    private static final Logger LOG = LogManager.getLogger();

    private Map<String,User> users = new HashMap<String, User>();

    public CustomInMemoryUserDetailsManager(Collection<User> arg) {
        LOG.info("Using inMemory UserDetailsService");
        arg.forEach((u) -> users.put(u.getUsername().toLowerCase(),u));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username.toLowerCase());
        LOG.debug(Markers.SECURITY_AUTH,"loading user {} from in-memory db",user);

        if(user == null)
        {
            throw new UsernameNotFoundException(username);
        }

        User userNew = new User(user.getUsername(),user.getPassword(),user.getAuthorities());
        return userNew;
    }

}
