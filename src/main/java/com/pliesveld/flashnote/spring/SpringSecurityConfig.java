package com.pliesveld.flashnote.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * Profile based security configuration
 * http://stackoverflow.com/questions/24827963/enabling-websecurityconfigurer-via-profile-does-not-work
 */

@Configuration
public class SpringSecurityConfig {

    @Profile("default")
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    @ConditionalOnExpression("!${my.security.enabled:false}")
    protected static class DefaultWebSecurity extends WebSecurityConfigurerAdapter {
    
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
        public void configure(WebSecurity web) throws Exception 
        {
            web
                .ignoring()
                .antMatchers("/resources/**");
        };
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
    
    
           http
                    .httpBasic().realmName("FlashNotes")
                    .and()
                    .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/deck/**","/students/**").permitAll();
    
    
    /*
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
            //.and()
            //        .rememberMe().rememberMeServices(tokenBasedRememberMeService);
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
    
    

    @Profile("local")
    @EnableWebSecurity
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @ConditionalOnExpression("${my.security.enabled:false}")
    protected static class LocalWebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                    .withUser("student").password("student").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("USER","ADMIN");
        }
        
        @Override
        public void configure(WebSecurity web) throws Exception 
        {
            web
                .ignoring()
                .antMatchers("/css/**","/js/**");
        };
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http 
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/index.html","/error").permitAll()
                .anyRequest().authenticated();
            
            http
                .formLogin().loginPage("/login.html").permitAll()
                .and()
                .logout().permitAll();
        }
    }

}
