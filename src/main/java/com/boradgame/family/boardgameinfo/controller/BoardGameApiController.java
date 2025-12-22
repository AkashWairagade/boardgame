// File: src/main/java/com/example/boardgames/controller/BoardGameApiController.java
package com.boradgame.family.boardgameinfo.controller;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import com.boradgame.family.boardgameinfo.repository.BoardGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boardgames")
@Slf4j
public class BoardGameApiController {
    private final BoardGameRepository repo;
    public BoardGameApiController(BoardGameRepository repo) { this.repo = repo; }

    @GetMapping
    public List<BoardGame> all() {

        List<BoardGame> all = repo.findAll();
        log.info("printing list of all BoardGames {}",all);
        return all;
    }
}