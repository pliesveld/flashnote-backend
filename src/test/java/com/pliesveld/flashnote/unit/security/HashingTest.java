package com.pliesveld.flashnote.unit.security;


import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.SecureRandom;

/**
 * Created by happs on 1/20/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
public class HashingTest {

    SecureRandom secureRandom;

    @Before
    public void setupRandomSeed()
    {
        secureRandom = new SecureRandom();
    }

    @Test
    public void testEncoder()
    {
        String password = "password";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10,secureRandom);
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("password hashed to " + hashedPassword);
        System.out.println("length: " + hashedPassword.length());

        passwordEncoder.matches(password,hashedPassword);
    }

}
