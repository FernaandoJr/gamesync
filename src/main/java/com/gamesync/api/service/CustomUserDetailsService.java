package com.gamesync.api.service;

import com.gamesync.api.model.User;
import com.gamesync.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação customizada da interface UserDetailsService do Spring Security.
 * Esta classe é responsável por carregar os detalhes de um usuário (como nome, senha e permissões)
 * a partir do banco de dados (via UserRepository) para que o Spring Security possa realizar a autenticação.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService { //
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
        return userRepository.findByUsername(username) //
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username)); //
    }
}