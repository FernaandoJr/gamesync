package com.gamesync.api.controller;

import com.gamesync.api.model.Game;
import com.gamesync.api.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/games")
public class ApiController {
    private final GameRepository gameRepository;

    public ApiController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        System.out.println("Game object: " + game);

        // Convert LocalDateTime to OffsetDateTime with a specific ZoneOffset
        game.setAddedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return ResponseEntity.ok(gameRepository.save(game));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game updated) {
        return gameRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setDescription(updated.getDescription());
                    existing.setDeveloper(updated.getDeveloper());
                    existing.setHoursPlayed(updated.getHoursPlayed());
                    existing.setFavorite(updated.isFavorite());
                    existing.setGenres(updated.getGenres());
                    existing.setTags(updated.getTags());
                    existing.setPlatforms(updated.getPlatforms());
                    existing.setAddedAt(updated.getAddedAt());
                    existing.setStatus(updated.getStatus());
                    existing.setSource(updated.getSource());
                    existing.setSteam(updated.getSteam());
                    return ResponseEntity.ok(gameRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
