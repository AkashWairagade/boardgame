package com.boradgame.family.boardgameinfo.controller;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import com.boradgame.family.boardgameinfo.repository.BoardGameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/boardgames")
public class BoardGameController {
    private final BoardGameRepository repo;

    public BoardGameController(BoardGameRepository repo) { this.repo = repo; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("games", repo.findAll());
        return "boardgames/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("boardGame", new BoardGame());
        return "boardgames/form";
    }

    @PostMapping
    public String save(@ModelAttribute BoardGame boardGame) {
        repo.save(boardGame);
        return "redirect:/boardgames";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Optional<BoardGame> opt = repo.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("boardgame", opt.get());
            return "boardgames/form";
        } else {
            return "redirect:/boardgames";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardGame boardGame) {
        repo.update(boardGame);
        return "redirect:/boardgames";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        repo.deleteById(id);
        return "redirect:/boardgames";
    }

    @GetMapping("/form.html")
    public String legacyFormHtml(Model model) {
        model.addAttribute("boardGame", new BoardGame());
        return "boardgames/form";
    }


}