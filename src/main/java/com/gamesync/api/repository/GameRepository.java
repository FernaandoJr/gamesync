package com.gamesync.api.repository;

import com.gamesync.api.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByUserId(String userId);

    boolean existsByNameAndUserId(String name, String userId);
}