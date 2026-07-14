package it.aulab.blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String CSP_DIRECTIVES = "default-src 'self'; " +
            "img-src 'self' data:; " +
            "script-src 'self' https://cdn.jsdelivr.net 'unsafe-inline'; " +
            "style-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com 'unsafe-inline'; " +
            "font-src 'self' https://cdnjs.cloudflare.com data:;";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userManager(
            PasswordEncoder passwordEncoder) {
        UserBuilder user = User
                .withUsername("user")
                .password(passwordEncoder.encode("12345678"))
                .roles("USER");

        UserBuilder admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin12345678"))
                .roles("USER", "ADMIN");

        return new InMemoryUserDetailsManager(
                user.build(),
                admin.build());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(
                "/",
                "/index.html",
                "/login",
                "/error")
                .permitAll()
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/favicon.ico")
                .permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/authors", true)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**"))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(CSP_DIRECTIVES)));

        return http.build();
    }
}