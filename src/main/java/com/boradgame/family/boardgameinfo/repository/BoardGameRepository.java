package com.boradgame.family.boardgameinfo.repository;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardGameRepository {
    private final JdbcTemplate jdbc;

    public BoardGameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<BoardGame> findAll() {
        String sql = "SELECT * FROM boardgames ORDER BY sr_no";
        return jdbc.query(sql, new BoardGameRowMapper());
    }

    public Optional<BoardGame> findById(int id) {
        String sql = "SELECT * FROM boardgames WHERE sr_no = ?";
        return jdbc.query(sql, new BoardGameRowMapper(), id).stream().findFirst();
    }

    public int save(BoardGame b) {
        String sql = "INSERT INTO boardgames (name,type,avg_age,description,players,avg_play_time_min,price_usd,purchase,comment,avg_bgg_rating,avg_complexity_weight,game_mechanics) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, b.getName());
            ps.setString(2, b.getType());
            ps.setString(3, b.getAvgAge());
            ps.setString(4, b.getDescription());
            ps.setString(5, b.getPlayers());
            if (b.getAvgPlayTimeMin() != null) ps.setString(6, b.getAvgPlayTimeMin()); else ps.setObject(6, null);
            ps.setBigDecimal(7, b.getPriceUSD());
            ps.setString(8, b.getPurchase());
            ps.setString(9, b.getComment());
            ps.setBigDecimal(10, b.getAvgBGGRating());
            ps.setBigDecimal(11, b.getAvgComplexityWeight());
            ps.setString(12, b.getGameMechanics());
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public int update(BoardGame b) {
        String sql = "UPDATE boardgames SET name=?,type=?,avg_age=?,description=?,players=?,avg_play_time_min=?,price_usd=?,purchase=?,comment=?,avg_bgg_rating=?,avg_complexity_weight=?,game_mechanics=? WHERE sr_no=?";
        return jdbc.update(sql,
                b.getName(), b.getType(), b.getAvgAge(), b.getDescription(), b.getPlayers(),
                b.getAvgPlayTimeMin(), b.getPriceUSD(), b.getPurchase(), b.getComment(),
                b.getAvgBGGRating(), b.getAvgComplexityWeight(), b.getGameMechanics(),
                b.getSrNo());
    }

    public int deleteById(int id) {
        return jdbc.update("DELETE FROM boardgames WHERE sr_no = ?", id);
    }
}