// File: src/main/java/com/gamesync/api/service/UserService.java
package com.gamesync.api.service;
import com.gamesync.api.dto.UserRegistrationDTO; // DTO para dados de registro de novo usuário.
import com.gamesync.api.dto.UserUpdateDTO;       // DTO para dados de atualização de usuário existente.
import com.gamesync.api.exception.DuplicateResourceException; // Exceção para quando um recurso já existe (ex: email duplicado).
import com.gamesync.api.exception.ResourceNotFoundException;  // Exceção para quando um recurso não é encontrado.
import com.gamesync.api.model.User; // Entidade que representa um usuário no sistema.
import com.gamesync.api.repository.UserRepository; // Interface para operações CRUD com usuários no MongoDB.
import org.springframework.security.core.Authentication; // Representa o token para uma requisição de autenticação ou um usuário autenticado.
import org.springframework.security.core.context.SecurityContextHolder; // Fornece acesso ao contexto de segurança.
import org.springframework.security.crypto.password.PasswordEncoder; // Interface para codificar senhas.
import org.springframework.stereotype.Service; // Indica que esta classe é um componente de serviço gerenciado pelo Spring.
import org.springframework.transaction.annotation.Transactional; // Usada para o método deleteUser, garantindo atomicidade.
import java.util.Collections; // Para criar listas imutáveis (ex: lista de roles).
import java.util.List;        // Interface para listas.
import java.util.Optional;    // Contêiner que pode ou não conter um valor não-nulo.

/**
 * Classe de serviço responsável pela lógica de negócios relacionada aos usuários.
 * Isso inclui registro, atualização, exclusão e busca de usuários,
 * além de interações com o contexto de segurança do Spring.
 */
@Service // Anotação que marca esta classe como um Bean de serviço do Spring.
public class UserService {

    // Dependência do repositório de usuários para acesso aos dados.
    private final UserRepository userRepository;
    // Dependência para codificar senhas antes de salvá-las.
    private final PasswordEncoder passwordEncoder;
    // Dependência do serviço de jogos, para operações como excluir jogos de um usuário.
    private final GameService gameService;

    /**
     * Construtor para injeção de dependências.
     * O Spring injetará instâncias de UserRepository, PasswordEncoder e GameService.
     * @param userRepository Repositório para operações de persistência de usuários.
     * @param passwordEncoder Codificador para senhas de usuários.
     * @param gameService Serviço para gerenciar lógica de negócios de jogos.
     */
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       GameService gameService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gameService = gameService;
    }

    /**
     * Método privado auxiliar para obter o objeto User do usuário atualmente autenticado
     * a partir do contexto de segurança do Spring.
     * @return O objeto User autenticado.
     * @throws IllegalStateException Se nenhum usuário estiver autenticado ou o tipo do principal for inválido.
     */
    private User getAuthenticatedUserInternal() {
        // Obtém o objeto Authentication do contexto de segurança.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Verifica se há uma autenticação válida e se o principal (usuário) é uma instância da nossa classe User.
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado ou tipo de principal inválido.");
        }
        // Converte e retorna o principal para o tipo User.
        return (User) authentication.getPrincipal();
    }

    /**
     * Registra um novo usuário no sistema.
     * @param registrationDTO DTO contendo os dados para registro do novo usuário.
     * @return O objeto User do usuário recém-criado e salvo.
     * @throws DuplicateResourceException Se o nome de usuário, email ou Steam ID já existirem.
     */
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Verifica se o nome de usuário já está em uso.
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + registrationDTO.getUsername() + "' já existe.");
        }
        // Verifica se o email já está registrado.
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + registrationDTO.getEmail() + "' já registrado.");
        }
        // Verifica se o Steam ID (se fornecido) já está registrado.
        if (registrationDTO.getSteamId() != null && !registrationDTO.getSteamId().isBlank() &&
                userRepository.findBySteamId(registrationDTO.getSteamId()).isPresent()) {
            throw new DuplicateResourceException("Steam ID '" + registrationDTO.getSteamId() + "' já registrado.");
        }

        // Cria uma nova instância de User.
        User newUser = new User();
        // Define os campos do novo usuário com base nos dados do DTO.
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        // Codifica a senha antes de salvá-la.
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        // Define o Steam ID se ele foi fornecido.
        if (registrationDTO.getSteamId() != null && !registrationDTO.getSteamId().isBlank()) {
            newUser.setSteamId(registrationDTO.getSteamId());
        }
        // Atribui a role padrão "ROLE_USER" para novos usuários.
        newUser.setRoles(Collections.singletonList("ROLE_USER"));

        // Salva o novo usuário no banco de dados.
        return userRepository.save(newUser);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * Apenas o usuário autenticado pode atualizar seus próprios dados.
     * @param userId ID do usuário a ser atualizado.
     * @param userUpdateDTO DTO contendo os dados a serem atualizados.
     * @return Um Optional contendo o User atualizado, ou Optional.empty() se não encontrado.
     * @throws ResourceNotFoundException Se o usuário autenticado tentar atualizar outro usuário.
     * @throws DuplicateResourceException Se o novo nome de usuário ou email já estiverem em uso por outro usuário.
     */
    public Optional<User> updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        // Obtém o usuário autenticado.
        User authenticatedUser = getAuthenticatedUserInternal();

        // Garante que o usuário autenticado só pode atualizar seu próprio perfil.
        if (!authenticatedUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("Acesso negado para atualizar este usuário ou usuário não encontrado.");
        }

        // Busca o usuário existente pelo ID.
        return userRepository.findById(userId)
                .map(existingUser -> { // Se o usuário existir, aplica as atualizações.
                    // Atualiza o nome de usuário se fornecido e diferente do atual.
                    if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isBlank()) {
                        // Verifica se o novo nome de usuário já existe (e não é o nome de usuário atual).
                        if (!existingUser.getUsername().equalsIgnoreCase(userUpdateDTO.getUsername()) &&
                                userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
                            throw new DuplicateResourceException("Novo nome de usuário '" + userUpdateDTO.getUsername() + "' já existe.");
                        }
                        existingUser.setUsername(userUpdateDTO.getUsername());
                    }

                    // Atualiza o email se fornecido e diferente do atual.
                    if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isBlank()) {
                        // Verifica se o novo email já existe (e não é o email atual).
                        if (!existingUser.getEmail().equalsIgnoreCase(userUpdateDTO.getEmail()) &&
                                userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent()) {
                            throw new DuplicateResourceException("Novo email '" + userUpdateDTO.getEmail() + "' já registrado.");
                        }
                        existingUser.setEmail(userUpdateDTO.getEmail());
                    }

                    // Atualiza a senha se uma nova senha for fornecida.
                    if (userUpdateDTO.getNewPassword() != null && !userUpdateDTO.getNewPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
                    }
                    // Salva as alterações no usuário.
                    return userRepository.save(existingUser);
                });
    }

    /**
     * Exclui um usuário do sistema.
     * Apenas o usuário autenticado pode excluir sua própria conta.
     * Também exclui todos os jogos associados a este usuário.
     * @param userId ID do usuário a ser excluído.
     * @return true se o usuário foi excluído com sucesso, false caso contrário (ex: usuário não encontrado).
     * @throws ResourceNotFoundException Se o usuário autenticado tentar excluir outro usuário.
     */
    @Transactional // Garante que a exclusão do usuário e seus jogos seja uma operação atômica.
    public boolean deleteUser(String userId) {
        // Obtém o usuário autenticado.
        User authenticatedUser = getAuthenticatedUserInternal();

        // Garante que o usuário autenticado só pode excluir sua própria conta.
        if (!authenticatedUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("Acesso negado para excluir este usuário ou usuário não encontrado.");
        }

        // Verifica se o usuário existe antes de tentar excluir.
        if (userRepository.existsById(userId)) {
            // Antes de excluir o usuário, exclui todos os jogos associados a ele.
            gameService.deleteAllGamesByUserId(userId);
            // Exclui o usuário do banco de dados.
            userRepository.deleteById(userId);
            return true; // Retorna true indicando sucesso.
        }
        return false; // Retorna false se o usuário não foi encontrado.
    }

    /**
     * Busca um usuário pelo seu nome de usuário.
     * @param username O nome de usuário a ser buscado.
     * @return Um Optional contendo o User, ou Optional.empty() se não encontrado.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Busca um usuário pelo seu ID.
     * Permite que o usuário autenticado busque seus próprios dados ou que um ADMIN busque qualquer usuário.
     * @param id O ID do usuário a ser buscado.
     * @return Um Optional contendo o User, ou Optional.empty() se não encontrado.
     * @throws ResourceNotFoundException Se o usuário não for encontrado ou o acesso for negado.
     */
    public Optional<User> findById(String id) {
        User authenticatedUser = getAuthenticatedUserInternal();
        // Verifica se o ID solicitado é do próprio usuário autenticado OU se o usuário autenticado é ADMIN.
        if (authenticatedUser.getId().equals(id) ||
                (authenticatedUser.getAuthorities() != null &&
                        authenticatedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))) {
            return userRepository.findById(id);
        }
        // Se não tiver permissão, lança exceção.
        throw new ResourceNotFoundException("Usuário não encontrado ou acesso negado.");
    }

    /**
     * Método público para obter o objeto User do usuário atualmente autenticado.
     * @return O objeto User autenticado.
     */
    public User getAuthenticatedUser() {
        return getAuthenticatedUserInternal();
    }
}