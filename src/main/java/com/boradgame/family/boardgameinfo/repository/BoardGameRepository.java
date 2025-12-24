package com.boradgame.family.boardgameinfo.repository;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BoardGameRepository {
    private final JdbcTemplate jdbc;

    public BoardGameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<BoardGame> findAll() {
        String sql = "SELECT * FROM boardgames ORDER BY purchase_date desc";
        return jdbc.query(sql, new BoardGameRowMapper());
    }

    // New: flexible findAll with filters and sort
    public List<BoardGame> findAll(Map<String, String> filters, String sortBy, String sortDir) {
        StringBuilder sql = new StringBuilder("SELECT * FROM boardgames");
        List<Object> args = new ArrayList<>();
        boolean first = true;
        for (Map.Entry<String, String> e : filters.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            if (v == null || v.trim().isEmpty()) continue;
            if (first) {
                sql.append(" WHERE ");
                first = false;
            } else {
                sql.append(" AND ");
            }
            // basic sanitization: allow only known filter columns
            String col = mapColumn(k);
            if (col == null) continue; // skip unknown filter
            sql.append(col).append(" LIKE ?");
            args.add("%" + v.trim() + "%");
        }

        String col = mapColumn(sortBy);
        if (col != null) {
            sql.append(" ORDER BY ").append(col);
            if ("desc".equalsIgnoreCase(sortDir)) sql.append(" DESC"); else sql.append(" ASC");
        } else {
            sql.append(" ORDER BY purchase_date desc");
        }

        return jdbc.query(sql.toString(), args.toArray(), new BoardGameRowMapper());
    }

    private String mapColumn(String requested) {
        if (requested == null) return null;
        Map<String, String> m = new HashMap<>();
        m.put("sr_no", "sr_no");
        m.put("srNo", "sr_no");
        m.put("id", "sr_no");
        m.put("name", "name");
        m.put("type", "type");
        m.put("avg_age", "avg_age");
        m.put("description", "description");
        m.put("players", "players");
        m.put("avg_play_time_min", "avg_play_time_min");
        m.put("avgPlayTimeMin", "avg_play_time_min");
        m.put("price_usd", "price_usd");
        m.put("priceUSD", "price_usd");
        m.put("purchase", "purchase");
        m.put("purchase_date", "purchase_date");
        m.put("comment", "comment");
        m.put("avg_bgg_rating", "avg_bgg_rating");
        m.put("avgBGGRating", "avg_bgg_rating");
        m.put("avg_complexity_weight", "avg_complexity_weight");
        m.put("avgComplexityWeight", "avg_complexity_weight");
        m.put("game_mechanics", "game_mechanics");
        return m.get(requested);
    }

    public Optional<BoardGame> findById(int id) {
        String sql = "SELECT * FROM boardgames WHERE sr_no = ?";
        return jdbc.query(sql, new BoardGameRowMapper(), id).stream().findFirst();
    }

    public int save(BoardGame b) {
        String sql = "INSERT INTO boardgames (name,type,avg_age,description,players,avg_play_time_min,price_usd,purchase,purchase_date,comment,avg_bgg_rating,avg_complexity_weight,game_mechanics) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            if (b.getPurchaseDate() != null) ps.setDate(9, java.sql.Date.valueOf(b.getPurchaseDate())); else ps.setObject(9, null);
            ps.setString(10, b.getComment());
            ps.setBigDecimal(11, b.getAvgBGGRating());
            ps.setBigDecimal(12, b.getAvgComplexityWeight());
            ps.setString(13, b.getGameMechanics());
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public int update(BoardGame b) {
        String sql = "UPDATE boardgames SET name=?,type=?,avg_age=?,description=?,players=?,avg_play_time_min=?,price_usd=?,purchase=?,purchase_date=?,comment=?,avg_bgg_rating=?,avg_complexity_weight=?,game_mechanics=? WHERE sr_no=?";
        return jdbc.update(sql,
                b.getName(), b.getType(), b.getAvgAge(), b.getDescription(), b.getPlayers(),
                b.getAvgPlayTimeMin(), b.getPriceUSD(), b.getPurchase(),
                b.getPurchaseDate() != null ? java.sql.Date.valueOf(b.getPurchaseDate()) : null,
                b.getComment(), b.getAvgBGGRating(), b.getAvgComplexityWeight(), b.getGameMechanics(),
                b.getSrNo());
    }

    public int deleteById(int id) {
        return jdbc.update("DELETE FROM boardgames WHERE sr_no = ?", id);
    }
}