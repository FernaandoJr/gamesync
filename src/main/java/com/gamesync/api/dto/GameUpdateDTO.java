// File: src/main/java/com/gamesync/api/dto/GameUpdateDTO.java
package com.gamesync.api.dto;

import com.gamesync.api.model.GameStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 * Data Transfer Object (DTO) para encapsular os dados que podem ser atualizados
 * em um jogo existente.
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da
 * API para atualização de jogos.
 *
 * <p>
 * Diferente do DTO de criação, os campos aqui são geralmente opcionais,
 * permitindo que o cliente envie apenas os dados que deseja modificar. A lógica
 * de serviço
 * tratará quais campos foram fornecidos e aplicará as atualizações
 * correspondentes.
 * </p>
 *
 * <p>
 * As anotações de validação aplicadas aos campos garantem que, se um valor for
 * fornecido, ele deve atender aos critérios especificados (ex: tamanho máximo).
 * </p>
 * 
 * <p>
 * Usando Lombok para reduzir código boilerplate.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameUpdateDTO {

	@Size(max = 255, message = "Game name must be up to 255 characters.")
	private String name;

	@Size(max = 2000, message = "Description must be up to 2000 characters.")
	private String description;

	@Size(max = 100, message = "Developer name must be up to 100 characters.")
	private String developer;

	@Size(max = 1024, message = "Image URL must be up to 1024 characters.")
	private String imageUrl;

	@PositiveOrZero(message = "Hours played must be zero or positive.")
	private Integer hoursPlayed;

	private Boolean favorite;

	private Set<@Size(max = 50) String> genres;

	private Set<@Size(max = 50) String> tags;

	private Set<@Size(max = 50) String> platforms;

	private GameStatus status;
}