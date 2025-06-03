// File: src/main/java/com/gamesync/api/config/SecurityConfig.java
package com.gamesync.api.config;

import com.gamesync.api.service.CustomUserDetailsService; // Nosso serviço que busca usuários para o Spring Security.
import org.springframework.context.annotation.Bean; // Anotação para declarar um método que produz um bean gerenciado pelo Spring.
import org.springframework.context.annotation.Configuration; // Indica que a classe declara um ou mais métodos @Bean e pode ser processada pelo container Spring.
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Interface central para processar uma requisição de autenticação.
import org.springframework.security.authentication.ProviderManager;      // Implementação comum de AuthenticationManager que delega para uma lista de AuthenticationProviders.
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Um AuthenticationProvider que recupera detalhes do usuário de um UserDetailsService.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Permite configurar a segurança baseada na web (regras de firewall).
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita o suporte à segurança web do Spring Security.
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Classe base para configurar aspectos do HttpSecurity de forma concisa (usada para desabilitar CSRF).
import org.springframework.security.config.http.SessionCreationPolicy; // Define políticas para criação de sessão (ex: STATELESS).
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementação de PasswordEncoder que usa o algoritmo BCrypt.
import org.springframework.security.crypto.password.PasswordEncoder;   // Interface para codificação e verificação de senhas.
import org.springframework.security.web.SecurityFilterChain;           // Interface que define uma cadeia de filtros aplicada a requisições HTTP.

/**
 * Classe de configuração para o Spring Security.
 * Define como a autenticação e autorização são tratadas na aplicação.
 * Habilita a segurança web e configura a cadeia de filtros de segurança,
 * o provedor de autenticação, e o codificador de senhas.
 */
@Configuration // Marca esta classe como uma fonte de definições de bean.
@EnableWebSecurity // Habilita a integração do Spring Security com o Spring MVC.
public class SecurityConfig {

    /**
     * Define um bean para o {@link PasswordEncoder}.
     * Utiliza o BCryptPasswordEncoder, que é um algoritmo forte e recomendado
     * para hashing de senhas. As senhas dos usuários serão codificadas usando este encoder
     * antes de serem salvas no banco de dados e verificadas durante o login.
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean // Este método produz um bean gerenciado pelo Spring.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna uma implementação de PasswordEncoder.
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
            CustomUserDetailsService userDetailsService, // Injetado pelo Spring.
            PasswordEncoder passwordEncoder             // Injetado pelo Spring.
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Define o serviço que carrega o usuário.
        authenticationProvider.setPasswordEncoder(passwordEncoder); // Define o codificador de senhas.
        // ProviderManager é uma implementação de AuthenticationManager que delega para uma lista de providers.
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

                // Configura as regras de autorização para as requisições HTTP.
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