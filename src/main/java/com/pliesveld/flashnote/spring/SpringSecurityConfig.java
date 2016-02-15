package com.pliesveld.flashnote.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new PlaintextPasswordEncoder());

        /* TODO: Encode password, salt with BCrypt hash
        // http://stackoverflow.com/questions/30805877/spring-authenticationmanagerbuilder-password-hashing
                .passwordEncoder(new BCryptPasswordEncoder());
        */

        /*
        // fetch with SQL
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select student_email,student_password from student_account where student_email=?")
                .authoritiesByUsernameQuery("select student_email,role from student_account where student_email=?");
        */
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/sign-up").permitAll()
                .antMatchers("/sign-in").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/loginprocess")
                .failureUrl("/mobile/app/sign-in?loginFailure=true")
                .permitAll();
        /*.and()
                .rememberMe().rememberMeServices(tokenBasedRememberMeService);
*/


//        super.configure(http);
        /*
        http
                .httpBasic().realmName("FlashNotes")
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/deck/**","/students/**").authenticated();
                */


    }
}
