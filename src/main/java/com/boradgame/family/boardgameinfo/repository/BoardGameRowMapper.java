package com.boradgame.family.boardgameinfo.repository;

import com.boradgame.family.boardgameinfo.model.BoardGame;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardGameRowMapper implements RowMapper<BoardGame> {
    @Override
    public BoardGame mapRow(ResultSet rs, int rowNum) throws SQLException {
        BoardGame b = new BoardGame();
        b.setSrNo(rs.getInt("sr_no"));
        b.setName(rs.getString("name"));
        b.setType(rs.getString("type"));
        b.setAvgAge(rs.getString("avg_age"));
        b.setDescription(rs.getString("description"));
        b.setPlayers(rs.getString("players"));
        b.setAvgPlayTimeMin(rs.getString("avg_play_time_min"));
        b.setPriceUSD(rs.getBigDecimal("price_usd"));
        b.setPurchase(rs.getString("purchase"));
        b.setComment(rs.getString("comment"));
        b.setAvgBGGRating(rs.getBigDecimal("avg_bgg_rating"));
        b.setAvgComplexityWeight(rs.getBigDecimal("avg_complexity_weight"));
        b.setGameMechanics(rs.getString("game_mechanics"));
        return b;
    }
}