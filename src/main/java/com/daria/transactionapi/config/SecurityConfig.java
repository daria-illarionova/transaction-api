package com.daria.transactionapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/* Updates:
Replaced .oauth2Login(oauth2 -> {}); with .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));
- .oauth2Login(oauth2 -> {}); was the temporary OAuth2-login configuration
- .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})); new JWT Resource Server authentication
    - Spring Security is now saying: "For protected endpoints, expect a bearer token and validate its JWT."

*/
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/transactions/**")
                        .authenticated()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }
}

/*  Replacing the previous/temporary HTTP Basic & Connecting Cognito to Spring Security

package com.daria.transactionapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    Tells Spring: Whenever password hashing/verification is required, use BCrypt.
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    UserDetailsService - creates one LOCAL user
        username: 
        password:
        role: USER

    .password(passwordEncoder.encode("local-password"))
        - Means that Spring does not keep local-password as the stored credential
        - Spring creates a BCrypt hash. 
    
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        var user = User.builder()
                .username("")
                .password(passwordEncoder.encode("local-password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    
    !!! SecurityFilterChain !!!
    - Spring Security places filters in front of our Controller.
    - The request goes through: 
        HTTP
        ↓
        Spring Security Filter Chain
        ↓
        Authentication
        ↓
        Authorization
        ↓
        Controller

    .requestMatchers("/api/v1/transactions/**") AND .hasRole("USER")
        - Means "Requests to the Transaction API require an authenticated user with the USER role."

    .anyRequest() AND .authenticated()
        - Means other requests also require authentication.
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/transactions/**")
                        .hasRole("USER")
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
} */