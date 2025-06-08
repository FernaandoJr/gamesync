package com.gamesync.api.config;

import com.gamesync.api.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração para o Spring Security.
 * Define como a autenticação e autorização são tratadas na aplicação.
 * Habilita a segurança web e configura a cadeia de filtros de segurança,
 * o provedor de autenticação, e o codificador de senhas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; // Injeção

    // Construtor para injetar o CustomAuthenticationEntryPoint
    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    /**
     * Define um bean para o {@link PasswordEncoder}.
     * Utiliza o BCryptPasswordEncoder, que é um algoritmo forte e recomendado
     * para hashing de senhas. As senhas dos usuários serão codificadas usando este encoder
     * antes de serem salvas no banco de dados e verificadas durante o login.
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define um bean para o {@link AuthenticationManager}.
     * O AuthenticationManager é responsável por processar as tentativas de autenticação.
     * Aqui, ele é configurado com um {@link DaoAuthenticationProvider}, que utiliza o
     * {@link CustomUserDetailsService} para carregar os dados do usuário e o
     * {@link PasswordEncoder} para verificar a senha.
     * @param userDetailsService Nosso serviço customizado que busca usuários do banco de dados.
     * @param passwordEncoder O codificador de senhas definido acima.
     * @return Uma instância de ProviderManager configurada com o DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Define a cadeia de filtros de segurança (SecurityFilterChain).
     * Esta é a configuração central da segurança web, onde se define quais requisições
     * são permitidas ou bloqueadas, como a autenticação é tratada, gerenciamento de sessão, etc.
     * @param http O objeto HttpSecurity usado para construir a cadeia de filtros.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/webjars/**" // Geralmente necessário para recursos estáticos do Swagger UI
                        ).permitAll() //
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll() // Permite registro sem autenticação
                        .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Usa nosso entry point customizado
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}