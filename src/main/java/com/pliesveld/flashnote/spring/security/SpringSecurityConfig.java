package com.pliesveld.flashnote.spring.security;

import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.security.JwtAuthenticationEntryPoint;
import com.pliesveld.flashnote.security.JwtAuthenticationTokenFilter;
import com.pliesveld.flashnote.spring.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import java.util.Collection;

import static com.pliesveld.flashnote.logging.Markers.SECURITY_INIT;

/*
 * Profile based security configuration
 * http://stackoverflow.com/questions/24827963/enabling-websecurityconfigurer-via-profile-does-not-work
 */

@Configuration
@Profile(Profiles.AUTH)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan(basePackageClasses = {
        com.pliesveld.flashnote.security.StudentPrincipal.class,
        com.pliesveld.flashnote.spring.security.SpringSecurityConfig.class
})
@PropertySource("")
public class SpringSecurityConfig {
    private static final Logger LOG = LogManager.getLogger();

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MODERATOR and ROLE_MODERATOR > ROLE_PREMIUM and ROLE_PREMIUM > ROLE_USER and ROLE_USER > ROLE_ACCOUNT");

        if(LOG.isDebugEnabled(SECURITY_INIT))
        {
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
                LOG.debug(SECURITY_INIT, sb.toString());
            }
        }


        return roleHierarchy;
    }

    @Configuration
    @Profile(Profiles.AUTH)
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    @ComponentScan(basePackageClasses = {
            com.pliesveld.flashnote.security.StudentPrincipal.class,
            com.pliesveld.flashnote.spring.security.SpringSecurityConfig.class

    })
    protected static class ProductionWebSecurity extends WebSecurityConfigurerAdapter {

        @Inject
        UserDetailsService userDetailsService;

        @Autowired
        RoleHierarchyImpl roleHierarchy;

        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder(11);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(this.userDetailsService)
                    .passwordEncoder(this.passwordEncoder());
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
            return new JwtAuthenticationEntryPoint();
        }

        private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
            DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
            return defaultWebSecurityExpressionHandler;
        }

//        @Qualifier("myAuthenticationManager")
//        @Autowired
//        AuthenticationManager authenticationManager;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
            JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
            authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
            return authenticationTokenFilter;
        }

        @Value("${security.debug:false}")
        Boolean debug;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                .debug(debug)
                .ignoring().antMatchers("/js/**","/css/**","/img/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/account/sign-up", "/account/confirm", "/account/reset", "/account/reset/confirm").permitAll()
                    .antMatchers("/auth", "/refresh").permitAll()
                    .antMatchers("/user").authenticated()
                    .antMatchers(HttpMethod.POST, "/account/password").fullyAuthenticated()
                    .antMatchers(HttpMethod.HEAD, "/categories/**", "/attachments/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/", "/students/**", "/categories/**", "/attachments/**",
                            "/decks/**", "/statements/**", "/questions/**", "/answers/**")
                .permitAll()
                    .expressionHandler(webExpressionHandler())

                    .antMatchers("/anon/**").permitAll()
                    .antMatchers("/auth/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN");

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint());

            http.addFilterBefore(this.authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

            http.csrf().disable();

            http.headers().disable();

        }

/*

    Form based authentication configuration


        @Autowired
        RememberService rememberService;

        protected void configure(HttpSecurity http) throws Exception {


            http.authorizeRequests().antMatchers("/**").hasRole("USER");
            http.formLogin().permitAll();
            http.rememberMe().tokenRepository(this.rememberService).userDetailsService(this.userDetailsService);


            //http.httpBasic().realmName("FlashNote");
            //http.addFilter(this.authenticationFilter());

/*            http.formLogin()
 *                   .successHandler(this.savedRequestAwareAuthenticationSuccessHandler())
 *                   .permitAll();
 *
 *            http.rememberMe().tokenRepository(this.rememberService).userDetailsService(this.userDetailsService);
 *            //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
 */
/*
            @Bean
            public SavedRequestAwareAuthenticationSuccessHandler
            savedRequestAwareAuthenticationSuccessHandler() {

                SavedRequestAwareAuthenticationSuccessHandler auth
                        = new SavedRequestAwareAuthenticationSuccessHandler();
                auth.setTargetUrlParameter("targetUrl");
                return auth;
            }
*/


/*
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(this.passwordEncoder());
            return authProvider;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .authenticationProvider(this.authenticationProvider());
        }


        @Bean
        public MyAuthenticationFilter authenticationFilter() {
            MyAuthenticationFilter authFilter = new MyAuthenticationFilter();
            authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
            authFilter.setAuthenticationManager(authenticationManager);
            authFilter.setAuthenticationSuccessHandler(new MySuccessHandler("/app"));
            authFilter.setAuthenticationFailureHandler(new MyFailureHandler("/login?error=1"));
            authFilter.setUsernameParameter("username");
            authFilter.setPasswordParameter("password");
            return authFilter;
        }


    static class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
        private static final Logger LOG = LogManager.getLogger();
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

            Authentication auth =  super.attemptAuthentication(request, response);
            LOG.debug(Markers.SECURITY_AUTH, "MyAuthFilter principal returned {}", auth.getPrincipal());
            LOG.debug(Markers.SECURITY_AUTH, "MyAuthFilter.details {}", auth.getDetails());
            return auth;
        }
    }

    static class MySuccessHandler implements AuthenticationSuccessHandler {
        private static final Logger LOG = LogManager.getLogger();
        public MySuccessHandler(String s) {
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            LOG.debug("My success handler for {}", authentication.getPrincipal());

        }
    }

    static class MyFailureHandler implements AuthenticationFailureHandler {
        private static final Logger LOG = LogManager.getLogger();
        public MyFailureHandler(String s) {
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            LOG.debug("My failure handler for {}", exception.getMessage());

        }
    }
*/
    }
}







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