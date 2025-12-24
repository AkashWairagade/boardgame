package com.boradgame.family.boardgameinfo.controller;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import com.boradgame.family.boardgameinfo.repository.BoardGameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

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

        List<BoardGame> games = repo.findAll(filters, sortBy, sortDir);

        // Calculate average cost per game (average of priceUSD for games with non-null price)
        BigDecimal sum = BigDecimal.ZERO;
        int priceCount = 0;
        Map<Integer, BigDecimal> yearTotals = new HashMap<>();
        for (BoardGame g : games) {
            if (g.getPriceUSD() != null) {
                sum = sum.add(g.getPriceUSD());
                priceCount++;
            }
            // group by year for avg cost per year
            LocalDate pd = g.getPurchaseDate();
            if (pd != null && g.getPriceUSD() != null) {
                int year = pd.getYear();
                yearTotals.put(year, yearTotals.getOrDefault(year, BigDecimal.ZERO).add(g.getPriceUSD()));
            }
        }

        BigDecimal avgCostPerGame = priceCount > 0 ? sum.divide(BigDecimal.valueOf(priceCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal avgCostPerYear = BigDecimal.ZERO;
        if (!yearTotals.isEmpty()) {
            BigDecimal totalAcrossYears = BigDecimal.ZERO;
            for (BigDecimal v : yearTotals.values()) totalAcrossYears = totalAcrossYears.add(v);
            avgCostPerYear = totalAcrossYears.divide(BigDecimal.valueOf(yearTotals.size()), 2, RoundingMode.HALF_UP);
        }

        // formatted display strings
        String avgCostPerGameDisplay = "$" + avgCostPerGame.setScale(2, RoundingMode.HALF_UP).toString();
        String avgCostPerYearDisplay = "$" + avgCostPerYear.setScale(2, RoundingMode.HALF_UP).toString();

        model.addAttribute("games", games);
        model.addAttribute("filters", filters);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("avgCostPerGame", avgCostPerGame);
        model.addAttribute("avgCostPerYear", avgCostPerYear);
        model.addAttribute("avgCostPerGameDisplay", avgCostPerGameDisplay);
        model.addAttribute("avgCostPerYearDisplay", avgCostPerYearDisplay);
        model.addAttribute("yearsCount", yearTotals.size());
        model.addAttribute("gamesWithPriceCount", priceCount);
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