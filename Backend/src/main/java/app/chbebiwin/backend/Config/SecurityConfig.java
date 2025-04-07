package app.chbebiwin.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive la protection CSRF pour les APIs REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/Utilisateur/register", "/api/Utilisateur/login","/api/Utilisateur/getAll","/api/Utilisateur/get/{id}").permitAll() // Autoriser ces endpoints
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // ou .formLogin() si tu veux une UI HTML

        return http.build();
    }
}
