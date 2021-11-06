package com.example.sportintelligencetesimolettadavide;

public class Match {

    private final int id;
    private final String edition, firstPlayer, result, secondPlayer, duration;

    public Match(int id, String location, String firstPlayer, String result, String secondPlayer, String duration) {
        this.id = id;
        this.edition = location;
        this.firstPlayer = firstPlayer;
        this.result = result;
        this.secondPlayer = secondPlayer;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getEdition() {
        return edition;
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
