package com.boradgame.family.boardgameinfo.controller;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import com.boradgame.family.boardgameinfo.repository.BoardGameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/boardgames")
public class BoardGameController {
    private final BoardGameRepository repo;

    public BoardGameController(BoardGameRepository repo) { this.repo = repo; }

    // Map only the empty subpath so TrailingSlashController can handle `/boardgames/` explicitly
    @GetMapping("")
    public String list(@RequestParam Map<String, String> params, Model model) {
        // Extract sort params (if any)
        String sortBy = params.remove("sortBy");
        String sortDir = params.remove("sortDir");

        // pass remaining params as filters
        Map<String, String> filters = new HashMap<>(params);

        model.addAttribute("games", repo.findAll(filters, sortBy, sortDir));
        model.addAttribute("filters", filters);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "boardgames/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("boardgame", new BoardGame());
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
        model.addAttribute("boardgame", new BoardGame());
        return "boardgames/form";
    }


}