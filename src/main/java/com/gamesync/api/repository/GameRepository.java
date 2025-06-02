package com.gamesync.api.repository;

import com.gamesync.api.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {

    List<Game> findByUserId(String userId);
    void deleteByUserId(String userId);

}