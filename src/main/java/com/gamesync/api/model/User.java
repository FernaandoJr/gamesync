package com.gamesync.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade que representa um usuário no sistema GameSync.
 * Esta classe é mapeada para a coleção "users" no banco de dados MongoDB.
 * Além de armazenar informações do usuário (username, email, etc.),
 * ela também implementa a interface UserDetails do Spring Security,
 * permitindo que seja usada diretamente pelo framework de segurança para
 * autenticação e autorização.
 * 
 * Utilizando Lombok:
 * - @Getter/@Setter: Gera getters e setters para todos os campos
 * - @NoArgsConstructor: Gera construtor sem argumentos
 * - @AllArgsConstructor: Gera construtor com todos os argumentos
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
	@Id
	private String id;

	@Indexed(unique = true)
	private String username;
	private String password;
	@Indexed(unique = true)
	private String email;

	private List<String> roles;

	// --- Implementação dos métodos da interface UserDetails ---

	/**
	 * Retorna as autoridades (permissões/roles) concedidas ao usuário.
	 * O Spring Security usa isso para verificações de autorização.
	 * 
	 * @return Uma coleção de GrantedAuthority representando os papéis do usuário.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //
		if (this.roles == null) {
			return List.of();
		}
		return this.roles.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	/**
	 * Retorna a senha usada para autenticar o usuário.
	 * 
	 * @return A senha codificada do usuário.
	 */
	@Override
	public String getPassword() { //
		return this.password;
	}

	/**
	 * Retorna o nome de usuário usado para autenticar o usuário.
	 * 
	 * @return O nome de usuário.
	 */
	@Override
	public String getUsername() { //
		return this.username;
	}

	/**
	 * Indica se a conta do usuário expirou. Uma conta expirada não pode ser
	 * autenticada.
	 * 
	 * @return true se a conta do usuário for válida (não expirada), false caso
	 *         contrário.
	 */
	@Override
	public boolean isAccountNonExpired() { //
		return true; // Por padrão, as contas não expiram. Pode ser implementada lógica customizada
									// aqui.
	}

	/**
	 * Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não
	 * pode ser autenticado.
	 * 
	 * @return true se o usuário não estiver bloqueado, false caso contrário.
	 */
	@Override
	public boolean isAccountNonLocked() { //
		return true; // Por padrão, as contas não são bloqueadas. Pode ser implementada lógica
									// customizada aqui.
	}

	/**
	 * Indica se as credenciais do usuário (senha) expiraram. Credenciais expiradas
	 * impedem a autenticação.
	 * 
	 * @return true se as credenciais do usuário forem válidas (não expiradas),
	 *         false caso contrário.
	 */
	@Override
	public boolean isCredentialsNonExpired() { //
		return true; // Por padrão, as credenciais não expiram. Pode ser implementada lógica
									// customizada aqui.
	}

	/**
	 * Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado
	 * não pode ser autenticado.
	 * 
	 * @return true se o usuário estiver habilitado, false caso contrário.
	 */
	@Override
	public boolean isEnabled() { //
		return true; // Por padrão, os usuários são habilitados. Pode ser implementada lógica
									// customizada aqui.
	}

	/**
	 * Sobrescreve o método toString para fornecer uma representação textual do
	 * objeto User.
	 * Útil para logging e depuração. Exclui a senha por motivos de segurança.
	 * 
	 * @return Uma String representando o objeto User.
	 */
	@Override
	public String toString() { //
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' + ", email='" + email + '\'' +
				", roles=" + roles +
				'}';
	}
}