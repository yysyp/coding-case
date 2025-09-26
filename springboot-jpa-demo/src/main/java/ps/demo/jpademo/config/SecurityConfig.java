package ps.demo.jpademo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ps.demo.jpademo.config.JwtTokenUtil;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final JwtTokenUtil jwtTokenUtil;

    public SecurityConfig(SecurityProperties securityProperties, JwtTokenUtil jwtTokenUtil) {
        this.securityProperties = securityProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable())) // Disable X-Frame-Options for H2 Console
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(securityProperties.getWhitelist().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
