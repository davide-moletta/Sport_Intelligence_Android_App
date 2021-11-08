package com.example.sportintelligencetesimolettadavide;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private final int id;
    private final String location, firstPlayer, result, secondPlayer, duration;
    private String date = "";
    private List<String> matchStats = new ArrayList<>(), setsStats = new ArrayList<>(), setsHistory = new ArrayList<>(), setsFifteens = new ArrayList<>(), quotes = new ArrayList<>();

    public Match(int id, String location, String firstPlayer, String result, String secondPlayer, String duration) {
        this.id = id;
        this.location = location;
        this.firstPlayer = firstPlayer;
        this.result = result;
        this.secondPlayer = secondPlayer;
        this.duration = duration;
    }

    public Match(int id, String location, String firstPlayer, String result, String secondPlayer, String duration, String date, List<String> matchStats, List<String> setsStats, List<String> setsHistory, List<String> setsFifteens, List<String> quotes) {
        this.id = id;
        this.location = location;
        this.firstPlayer = firstPlayer;
        this.result = result;
        this.secondPlayer = secondPlayer;
        this.duration = duration;
        this.date = date;
        this.matchStats = matchStats;
        this.setsStats = setsStats;
        this.setsHistory = setsHistory;
        this.setsFifteens = setsFifteens;
        this.quotes = quotes;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getMatchStats() {
        return matchStats;
    }

    public List<String> getSetsStats() {
        return setsStats;
    }

    public List<String> getSetsHistory() {
        return setsHistory;
    }

    public List<String> getSetsFifteens() {
        return setsFifteens;
    }

    public List<String> getQuotes() {
        return quotes;
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
