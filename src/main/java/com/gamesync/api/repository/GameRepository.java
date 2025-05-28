package com.gamesync.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamesync.api.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}