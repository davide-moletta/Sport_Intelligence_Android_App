package com.example.sportintelligencetesimolettadavide;

import java.util.List;

public class Match {

    private final int id;
    private final String location, firstPlayer, result, secondPlayer, duration;
    private String field, round;
    private String date = "";
    private List[] setsStats, setsHistory, setsFifteens, setsTiebreaks;
    private List<Object> matchStats, quotes;

    public Match(int id, String location, String firstPlayer, String result, String secondPlayer, String duration) {
        this.id = id;
        this.location = location;
        this.firstPlayer = firstPlayer;
        this.result = result;
        this.secondPlayer = secondPlayer;
        this.duration = duration;
    }

    public Match(int id, String location, String firstPlayer, String result, String secondPlayer, String duration, String date, String field, String round,
                 List<Object> matchStats, List[] setsStats, List[] setsHistory, List[] setsFifteens, List[] setsTiebreaks, List<Object> quotes) {

        this.id = id;
        this.location = location;
        this.firstPlayer = firstPlayer;
        this.result = result;
        this.secondPlayer = secondPlayer;
        this.duration = duration;
        this.date = date;
        this.field = field;
        this.round = round;

        this.matchStats = matchStats;
        this.setsStats = setsStats;
        this.setsHistory = setsHistory;
        this.setsFifteens = setsFifteens;
        this.setsTiebreaks = setsTiebreaks;
        this.quotes = quotes;
    }

    public String getField() {
        return field;
    }

    public String getRound() {
        return round;
    }

    public String getDate() {
        return date;
    }

    public List[] getSetsStats() {
        return setsStats;
    }

    public List[] getSetsHistory() {
        return setsHistory;
    }

    public List[] getSetsFifteens() {
        return setsFifteens;
    }

    public List[] getSetsTiebreaks() {
        return setsTiebreaks;
    }

    public List<Object> getMatchStats() {
        return matchStats;
    }

    public List<Object> getQuotes() {
        return quotes;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public String getResult() {
        return result;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public String getDuration() {
        return duration;
    }
}
