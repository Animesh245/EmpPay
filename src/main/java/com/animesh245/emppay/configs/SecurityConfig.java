package com.animesh245.emppay.configs;

import com.animesh245.emppay.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
        private final PasswordEncoder passwordEncoder;
        private final UserService userService;
        private final AuthFilter authFilter;

        @Autowired
        protected void configure(AuthenticationManagerBuilder auth) throws Exception
        {
            auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
        }


        @Bean
        protected SecurityFilterChain configure(HttpSecurity http) throws Exception
        {
                http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(request -> request.requestMatchers("/**")
                                .permitAll().anyRequest().authenticated())
                        //.cors(AbstractHttpConfigurer::disable)             // spring security will handle cors before spring mvc, this is for not creating conflict. Handles this error: response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource
//                .antMatchers("/api/v1/employees/**", "/api/v1/projects/**", "/api/v1/departments/**", "/api/v1/dependents/**").hasAuthority("ROLE_ADMIN")
//                .antMatchers( HttpMethod.GET,"/api/v1/dependents/**", "/api/v1/employees/{id}").hasAuthority("ROLE_EMPLOYEE")
//                .antMatchers(HttpMethod.POST, "/api/v1/employees/{id}", "/api/v1/employees/**").hasAuthority("ROLE_EMPLOYEE")
//                .antMatchers("/api/v1/employees/**","/api/v1/projects/**", "/api/v1/departments/**","/api/v1/dependents/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                        .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
}
