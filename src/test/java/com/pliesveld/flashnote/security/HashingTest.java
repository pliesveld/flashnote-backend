package com.pliesveld.flashnote.security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by happs on 1/20/16.
 */
public class HashingTest {
    public static void main(String args[])
    {
        String password = "password";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("password hashed to " + hashedPassword);
        System.out.println("length: " + hashedPassword.length());

    }

}
