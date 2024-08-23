package prod.bookapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class Config {

    private static final String LOGIN_URL = "/login";
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationSuccessHandlerBasic customAuthenticationSuccessHandlerBasic;

    public Config(AuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomAuthenticationSuccessHandlerBasic customAuthenticationSuccessHandlerBasic) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationSuccessHandlerBasic = customAuthenticationSuccessHandlerBasic;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(antMatcher("/user/register")).permitAll()
                        .requestMatchers(antMatcher("/login")).permitAll()
                        .requestMatchers(antMatcher("/login/**")).permitAll()
                        .requestMatchers(antMatcher("/")).permitAll()
                        .requestMatchers(antMatcher("/timeslot/free")).permitAll()
                        .requestMatchers(antMatcher("/auth/status")).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2LoginConfigurer ->
                        oauth2LoginConfigurer
                                .successHandler(customAuthenticationSuccessHandler)
                                .loginPage(LOGIN_URL)
                                .permitAll()
                )
                .formLogin(formLoginConfigurer ->
                        formLoginConfigurer
                                .successHandler(customAuthenticationSuccessHandlerBasic)
                                .loginPage(LOGIN_URL)
                                .permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        return request -> {
            org.springframework.web.cors.CorsConfiguration config =
                    new org.springframework.web.cors.CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }
}
