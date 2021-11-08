package com.example.sportintelligencetesimolettadavide;

public class Filter {
    private final String name;
    private boolean matchInfo, matchStats, setStats, setHistory, quotes;

    public Filter(String name, boolean matchInfo, boolean matchStats, boolean setStats, boolean setHistory, boolean quotes) {
        this.name = name;
        this.matchInfo = matchInfo;
        this.matchStats = matchStats;
        this.setStats = setStats;
        this.setHistory = setHistory;
        this.quotes = quotes;
    }

    public String getName() {
        return name;
    }

    public boolean isMatchInfo() {
        return matchInfo;
    }

    public boolean isMatchStats() {
        return matchStats;
    }

    public boolean isSetStats() {
        return setStats;
    }

    public boolean isSetHistory() {
        return setHistory;
    }

    public boolean isQuotes() {
        return quotes;
    }
}
