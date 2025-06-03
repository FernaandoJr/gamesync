package com.gamesync.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority; // Interface que representa uma permissão/role concedida ao usuário.
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implementação concreta de GrantedAuthority.
import org.springframework.security.core.userdetails.UserDetails; // Interface do Spring Security que define os detalhes essenciais de um usuário.
import java.util.Collection; // Interface base para coleções.
import java.util.List;        // Interface para listas ordenadas.
import java.util.stream.Collectors; // Para operações com Streams (ex: transformar lista de Strings em lista de GrantedAuthority).

/**
 * Entidade que representa um usuário no sistema GameSync.
 * Esta classe é mapeada para a coleção "users" no banco de dados MongoDB.
 * Além de armazenar informações do usuário (username, email, etc.),
 * ela também implementa a interface UserDetails do Spring Security,
 * permitindo que seja usada diretamente pelo framework de segurança para autenticação e autorização.
 */
@Document(collection = "users") // Anotação do Spring Data MongoDB que especifica o nome da coleção no banco.
public class User implements UserDetails { // A classe User implementa UserDetails para se integrar com o Spring Security.
    @Id // Marca este campo como o identificador único do documento no MongoDB.
    private String id; // O ID único do usuário, geralmente gerado pelo MongoDB.

    @Indexed(unique = true) // Cria um índice único para o campo 'username', garantindo que não haja usernames duplicados.
    private String username; // Nome de usuário para login.
    private String password; // Senha do usuário (será armazenada de forma codificada).

    @Indexed(unique = true) // Cria um índice único para o campo 'email', garantindo que não haja emails duplicados.
    private String email;    // Endereço de email do usuário.

    // Cria um índice único para 'steamId', mas permite múltiplos documentos com 'steamId' nulo (sparse=true).
    // Útil se o Steam ID for opcional mas precisar ser único quando fornecido.
    @Indexed(unique = true, sparse = true) //
    private String steamId;  // ID do usuário na plataforma Steam (opcional).

    private List<String> roles; // Lista de papéis/perfis do usuário no sistema (ex: "ROLE_USER", "ROLE_ADMIN").

    /**
     * Construtor padrão.
     * Necessário para frameworks como Spring Data MongoDB e Jackson (para desserialização JSON).
     */
    public User() { //
    }

    /**
     * Construtor com campos essenciais para criar uma instância de User.
     * @param username O nome de usuário.
     * @param password A senha (ainda não codificada neste ponto, geralmente codificada pelo serviço antes de salvar).
     * @param email O email do usuário.
     * @param roles A lista de papéis/perfis do usuário.
     */
    public User(String username, String password, String email, List<String> roles) { //
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    // --- Implementação dos métodos da interface UserDetails ---

    /**
     * Retorna as autoridades (permissões/roles) concedidas ao usuário.
     * O Spring Security usa isso para verificações de autorização.
     * @return Uma coleção de GrantedAuthority representando os papéis do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //
        // Se a lista de roles for nula, retorna uma lista vazia de autoridades.
        if (this.roles == null) {
            return List.of(); // Ou Collections.emptyList() para compatibilidade com Java mais antigo.
        }
        // Converte a lista de Strings (nomes dos papéis) para uma lista de SimpleGrantedAuthority.
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new) // Equivalente a role -> new SimpleGrantedAuthority(role)
                .collect(Collectors.toList());
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     * @return A senha codificada do usuário.
     */
    @Override
    public String getPassword() { //
        return this.password;
    }

    /**
     * Retorna o nome de usuário usado para autenticar o usuário.
     * @return O nome de usuário.
     */
    @Override
    public String getUsername() { //
        return this.username;
    }

    /**
     * Indica se a conta do usuário expirou. Uma conta expirada não pode ser autenticada.
     * @return true se a conta do usuário for válida (não expirada), false caso contrário.
     */
    @Override
    public boolean isAccountNonExpired() { //
        return true; // Por padrão, as contas não expiram. Pode ser implementada lógica customizada aqui.
    }

    /**
     * Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não pode ser autenticado.
     * @return true se o usuário não estiver bloqueado, false caso contrário.
     */
    @Override
    public boolean isAccountNonLocked() { //
        return true; // Por padrão, as contas não são bloqueadas. Pode ser implementada lógica customizada aqui.
    }

    /**
     * Indica se as credenciais do usuário (senha) expiraram. Credenciais expiradas impedem a autenticação.
     * @return true se as credenciais do usuário forem válidas (não expiradas), false caso contrário.
     */
    @Override
    public boolean isCredentialsNonExpired() { //
        return true; // Por padrão, as credenciais não expiram. Pode ser implementada lógica customizada aqui.
    }

    /**
     * Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado não pode ser autenticado.
     * @return true se o usuário estiver habilitado, false caso contrário.
     */
    @Override
    public boolean isEnabled() { //
        return true; // Por padrão, os usuários são habilitados. Pode ser implementada lógica customizada aqui.
    }

    // --- Getters e Setters para os campos específicos da classe User ---
    // (getUsername e getPassword já são fornecidos pela implementação de UserDetails)

    public String getId() { return id; } //
    public void setId(String id) { this.id = id; } //

    // setUsername é necessário se o username puder ser alterado após a criação.
    public void setUsername(String username) { this.username = username; } //

    // setPassword é necessário para definir a senha (geralmente codificada pelo serviço).
    public void setPassword(String password) { this.password = password; } //

    public String getEmail() { return email; } //
    public void setEmail(String email) { this.email = email; } //

    public String getSteamId() { return steamId; } //
    public void setSteamId(String steamId) { this.steamId = steamId; } //

    public List<String> getRoles() { return roles; } //
    public void setRoles(List<String> roles) { this.roles = roles; } //

    /**
     * Sobrescreve o método toString para fornecer uma representação textual do objeto User.
     * Útil para logging e depuração. Exclui a senha por motivos de segurança.
     * @return Uma String representando o objeto User.
     */
    @Override
    public String toString() { //
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", steamId='" + steamId + '\'' +
                ", roles=" + roles +
                '}';
    }
}