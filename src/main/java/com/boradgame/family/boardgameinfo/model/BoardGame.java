package com.boradgame.family.boardgameinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardGame {
    private Integer srNo;
    private String name;
    private String type;
    private String avgAge;
    private String description;
    private String players;
    private String avgPlayTimeMin;
    private BigDecimal priceUSD;
    private String purchase;
    private String comment;
    private BigDecimal avgBGGRating;
    private BigDecimal avgComplexityWeight;
    private String gameMechanics;

}