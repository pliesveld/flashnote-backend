package com.pliesveld.flashnote.spring.security;

import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.spring.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import javax.inject.Inject;
import java.util.Collection;

/*
 * Profile based security configuration
 * http://stackoverflow.com/questions/24827963/enabling-websecurityconfigurer-via-profile-does-not-work
 */

@Configuration
@Profile(Profiles.AUTH)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = {
        com.pliesveld.flashnote.security.StudentPrincipal.class,
        com.pliesveld.flashnote.spring.security.SpringSecurityConfig.class
})
public class SpringSecurityConfig {
    private static final Logger LOG = LogManager.getLogger();

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MODERATOR and ROLE_MODERATOR > ROLE_PREMIUM and ROLE_PREMIUM > ROLE_USER and ROLE_USER > ROLE_ACCOUNT");

        for(StudentRole role : StudentRole.values())
        {
            StringBuilder sb = new StringBuilder();
            sb.append("for Role ");
            sb.append(role.name());

            sb.append(" has authorities ");
            Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role.name());
            authorities.forEach((auth) -> sb.append(auth));
            sb.append(" has reachable roles ");

            roleHierarchy.getReachableGrantedAuthorities(authorities).forEach((grantedAuthority) -> sb.append(grantedAuthority + " "));
            LOG.debug(sb.toString());
        }


        return roleHierarchy;
    }

    @Configuration
    @Profile(Profiles.AUTH)
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @ComponentScan(basePackageClasses = {
            com.pliesveld.flashnote.security.StudentPrincipal.class,
            com.pliesveld.flashnote.spring.security.SpringSecurityConfig.class

    })
    protected static class ProductionWebSecurity extends WebSecurityConfigurerAdapter {

        @Inject
        UserDetailsService userDetailsService;

        @Autowired RoleHierarchyImpl roleHierarchy;
        boolean debug_enable_bcrypt = true;

        @Bean
        PasswordEncoder passwordEncoder()
        {
            if(debug_enable_bcrypt) {
                return new BCryptPasswordEncoder();
            } else {
                return new NoOpPasswordEncoder();
            }
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            LOG.debug("Configuring userDetailsService: {}", userDetailsService.getClass().getName());
            auth.
                    userDetailsService(userDetailsService)
                    .passwordEncoder(new NoOpPasswordEncoder())
            .and()
            .authenticationEventPublisher(new AuthenticationEventPublisher() {

                @Override
                public void publishAuthenticationSuccess(Authentication authentication) {
                    LOG.debug("Authenticating from builder.");

                }

                @Override
                public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
                    LOG.debug("AuthenticatingFailure from builder.");

                }
            });
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.debug(true);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http


                .csrf().disable()
                    .httpBasic().and()
                .authorizeRequests()
                    .expressionHandler(webExpressionHandler())
                    .antMatchers("/", "/students").permitAll()
                    .antMatchers("/test/**").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();


        }

        private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
            DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
            return defaultWebSecurityExpressionHandler;
        }

    }
}

/* From Spring Security -- for testing */
class NoOpPasswordEncoder implements PasswordEncoder {

    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }

    /**
     * Get the singleton {@link NoOpPasswordEncoder}.
     */
    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();
}







            /*
           http
            .formLogin()
                    .loginPage("/")
                    .loginProcessingUrl("/loginprocess")
                    .failureUrl("/mobile/app/sign-in?loginFailure=true").and()
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                   .httpBasic().realmName("FlashNotes")
                   .and()
                   .authorizeRequests()
                   .antMatchers("/").permitAll()
                   .antMatchers("/sign-up").permitAll()
                   .antMatchers("/sign-in").permitAll()
                   .antMatchers("/login").permitAll()
                   .antMatchers("/test/**").authenticated()
                   .anyRequest().authenticated();

        }
    }*/

    
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
        

        


  /*

    @Profile("security-basic")
    protected static class LocalWebSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                    .withUser("student").password("student").roles("USER").authorities("USER")
                    .and()
                    .withUser("admin").password("admin").roles("USER","ADMIN").authorities("ADMIN");
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http
                    .httpBasic().realmName("FlashNotes")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .anyRequest().authenticated();
        }
    }


    @Profile("oauth2")
    @Configuration
    protected static class OAuthWebSecurity extends WebSecurityConfigurerAdapter {

        @Override
        @Bean
        protected AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }
    }

    @Profile("oauth2")
    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        @Inject AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient("flashnoteClient")
                    .secret("top_secret")
                    .authorizedGrantTypes("password")
                    .scopes("read","write")
                    .resourceIds("FlashNote_Resources");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//            endpoints.userDetailsService(userDetailsService);
            endpoints.authenticationManager(this.authenticationManager);
            //super.configure(endpoints);
        }
    }

    @Profile("oauth2")
    @Configuration
    @EnableResourceServer
    protected static class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("FlashNote_Resources");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .requestMatchers().antMatchers("/oauth2/secured/**")
                    .and()
                    .authorizeRequests().antMatchers("/oauth2/secured/**").authenticated();
        }
    }





}*/