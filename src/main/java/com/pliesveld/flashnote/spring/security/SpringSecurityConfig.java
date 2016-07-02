package com.pliesveld.flashnote.spring.security;

import com.pliesveld.flashnote.domain.AccountRole;
import com.pliesveld.flashnote.security.JwtAuthenticationEntryPoint;
import com.pliesveld.flashnote.security.JwtAuthenticationTokenFilter;
import com.pliesveld.flashnote.spring.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import java.util.Collection;

import static com.pliesveld.flashnote.logging.Markers.SECURITY_INIT;


@Configuration
public class SpringSecurityConfig {
    private static final Logger LOG = LogManager.getLogger();

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
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

        @Bean
        public RoleHierarchyImpl roleHierarchy() {
            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
            roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MODERATOR and ROLE_MODERATOR > ROLE_PREMIUM and ROLE_PREMIUM > ROLE_USER and ROLE_USER > ROLE_ACCOUNT");

            if (LOG.isDebugEnabled(SECURITY_INIT))
            {
                for (AccountRole role : AccountRole.values())
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
            defaultWebSecurityExpressionHandler.setRoleHierarchy(this.roleHierarchy());
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
    }

}
