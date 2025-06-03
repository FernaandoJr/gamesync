// File: src/main/java/com/gamesync/api/service/CustomUserDetailsService.java
package com.gamesync.api.service;

// Importação do modelo de dados do Usuário.
import com.gamesync.api.model.User; // Entidade User que também implementa UserDetails.

// Importação do repositório de Usuário para acesso aos dados.
import com.gamesync.api.repository.UserRepository; // Interface para operações de busca de usuários no MongoDB.

// Importações do Spring Security para carregar detalhes do usuário.
import org.springframework.security.core.userdetails.UserDetails;            // Interface do Spring Security que representa os detalhes de um usuário.
import org.springframework.security.core.userdetails.UserDetailsService;     // Interface do Spring Security para carregar dados específicos do usuário.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção lançada se um usuário não for encontrado.

// Importação para marcar a classe como um serviço do Spring.
import org.springframework.stereotype.Service; // Indica que esta classe é um componente de serviço gerenciado pelo Spring.

/**
 * Implementação customizada da interface UserDetailsService do Spring Security.
 * Esta classe é responsável por carregar os detalhes de um usuário (como nome, senha e permissões)
 * a partir do banco de dados (via UserRepository) para que o Spring Security possa realizar a autenticação.
 */
@Service // Anotação que marca esta classe como um Bean de serviço do Spring.
public class CustomUserDetailsService implements UserDetailsService { //

    // Dependência do repositório de usuários para buscar os dados do usuário no banco.
    private final UserRepository userRepository; //

    /**
     * Construtor para injeção de dependência do UserRepository.
     * O Spring injetará uma instância de UserRepository aqui.
     * @param userRepository O repositório para acesso aos dados dos usuários.
     */
    public CustomUserDetailsService(UserRepository userRepository) { //
        this.userRepository = userRepository;
    }

    /**
     * Carrega os detalhes de um usuário específico pelo seu nome de usuário.
     * Este método é chamado pelo Spring Security durante o processo de autenticação.
     * @param username O nome de usuário (login) cujos detalhes são solicitados.
     * @return Um objeto UserDetails contendo os dados do usuário. No nosso caso, a própria entidade User implementa UserDetails.
     * @throws UsernameNotFoundException Se nenhum usuário for encontrado com o nome de usuário fornecido.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //
        // Busca o usuário no banco de dados pelo nome de usuário usando o UserRepository.
        // O método findByUsername retorna um Optional<User>.
        // Se o usuário não for encontrado (Optional vazio), lança UsernameNotFoundException.
        // Caso contrário, retorna o objeto User encontrado (que implementa UserDetails).
        return userRepository.findByUsername(username) //
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username)); //
    }
}