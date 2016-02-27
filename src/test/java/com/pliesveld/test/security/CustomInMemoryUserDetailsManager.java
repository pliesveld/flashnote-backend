package com.pliesveld.test.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomInMemoryUserDetailsManager implements UserDetailsService {
    private Map<String,User> users = new HashMap<String, User>();

    public CustomInMemoryUserDetailsManager(Collection<User> arg) {
        arg.forEach((u) -> users.put(u.getUsername().toLowerCase(),u));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username.toLowerCase());

        if(user == null)
        {
            throw new UsernameNotFoundException(username);
        }

        User userNew = new User(user.getUsername(),user.getPassword(),user.getAuthorities());
        return userNew;
    }

}
