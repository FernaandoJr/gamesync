// File: src/main/java/com/gamesync/api/config/SecurityConfig.java
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
    @Bean //
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider); //
    }

    /**
     * Define a cadeia de filtros de segurança (SecurityFilterChain).
     * Esta é a configuração central da segurança web, onde se define quais requisições
     * são permitidas ou bloqueadas, como a autenticação é tratada, gerenciamento de sessão, etc.
     * @param http O objeto HttpSecurity usado para construir a cadeia de filtros.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean //
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita a proteção CSRF (Cross-Site Request Forgery).
                // Comum para APIs REST stateless que não usam sessões baseadas em cookies da mesma forma
                // que aplicações web tradicionais. Se a API for consumida por um frontend no mesmo domínio
                // e usar cookies para autenticação, CSRF pode ser relevante. Para autenticação baseada em token
                // ou HTTP Basic, geralmente é desabilitado.
                .csrf(AbstractHttpConfigurer::disable) //

                .authorizeHttpRequests(authorize -> authorize //
                        // Permite todas as requisições HTTP POST para o endpoint "/users/register"
                        // sem necessidade de autenticação. Isso é essencial para permitir que novos usuários se registrem.
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll() //
                        // Para qualquer outra requisição não especificada acima, exige autenticação.
                        .anyRequest().authenticated() //
                )

                // Habilita a autenticação HTTP Basic.
                // Isso significa que os clientes devem enviar um cabeçalho "Authorization"
                // com credenciais "Basic <base64_encoded_username:password>".
                .httpBasic(httpBasic -> {}) //

                // Configura o gerenciamento de sessão para ser STATELESS.
                // Para APIs REST, é crucial que cada requisição seja independente e não dependa
                // de uma sessão no servidor. O servidor não armazenará o estado da sessão do cliente.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //

        // Constrói e retorna a cadeia de filtros de segurança.
        return http.build();
    }
}