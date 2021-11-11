package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    private static final String FILE_FILTERS_NAME = "filters.txt";
    private static final String FILE_FAVOURITE_NAME = "favouriteMatches.txt";

    ImageView back, star;
    TextView tournamentName, tournamentInfo, firstPlayer, result, secondPlayer, duration, matchStatsLabel, setStatsLabel, setHistoryLabel, quotesLabel;
    ConstraintLayout matchStatsLayout, setStatsLayout, setHistoryLayout, quotesLayout;

    FileOperations fileFilters, fileFavouriteMatches;
    Neo4J neo4J;
    Match match;
    List<Object> matchStat, quotes;
    List[] setsStat, setsHistory, setsFifteens, setsTiebreaks;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        fileFilters = new FileOperations(FILE_FILTERS_NAME, view);
        fileFavouriteMatches = new FileOperations(FILE_FAVOURITE_NAME, view);
        neo4J = new Neo4J();

        int matchId = SearchResultFragmentArgs.fromBundle(getArguments()).getMatchId();

        back = view.findViewById(R.id.back);
        star = view.findViewById(R.id.star);

        tournamentName = view.findViewById(R.id.tournamentName);
        tournamentInfo = view.findViewById(R.id.tournamentInfo);
        firstPlayer = view.findViewById(R.id.firstPlayer);
        result = view.findViewById(R.id.result);
        secondPlayer = view.findViewById(R.id.secondPlayer);
        duration = view.findViewById(R.id.duration);

        matchStatsLabel = view.findViewById(R.id.matchStatsLabel);
        setStatsLabel = view.findViewById(R.id.setStatsLabel);
        setHistoryLabel = view.findViewById(R.id.setHistoryLabel);
        quotesLabel = view.findViewById(R.id.quotesLabel);

        matchStatsLayout = view.findViewById(R.id.matchStats);
        setStatsLayout = view.findViewById(R.id.setStats);
        setHistoryLayout = view.findViewById(R.id.setHistory);
        quotesLayout = view.findViewById(R.id.matchQuotes);

        String[] favouriteMatches = fileFavouriteMatches.load().split("\n");

        for (String favouriteMatch : favouriteMatches) {
            if (favouriteMatch.equals(String.valueOf(matchId))) {
                //star.setImageDrawable(ResourcesCompat.getDrawable());
            }
        }

        match = neo4J.fetchAllDataFromId(matchId);

        String editionAndDate = neo4J.fetchEditionFromId(matchId) + " - " + match.getDate();
        String tournamentInfoString = match.getLocation() + ", " + match.getField() + " - " + match.getRound();

        matchStat = match.getMatchStats();
        quotes = match.getQuotes();


        matchStatsLabel.setText(ObjectListToString(matchStat));

        setsStat = match.getSetsStats();
        setsFifteens = match.getSetsFifteens();
        setsHistory = match.getSetsHistory();
        setsTiebreaks = match.getSetsTiebreaks();


        List<String> setGames, setFifteens, setStat = new ArrayList<>(), setTiebreaks;

        for (List list : setsStat) {
            if (list != null) {
                String set = "";
                for (Object object : list) {
                    set += object.toString() + "\n\n";
                }
                setStat.add(set);
            }
        }
        System.out.println(setStat);


        tournamentName.setText(editionAndDate);
        tournamentInfo.setText(tournamentInfoString);
        firstPlayer.setText(match.getFirstPlayer());
        result.setText(match.getResult());
        secondPlayer.setText(match.getSecondPlayer());
        duration.setText(match.getDuration());


        //settare il codice per l'hide se null e impostare invece le scritte se esiste qualcosa nelle liste

        //Lettura da file per vedere se è un preferito

        neo4J.close();

        star.setOnClickListener(v -> fileFavouriteMatches.save(String.valueOf(matchId)));

        back.setOnClickListener(v -> navController.navigateUp());
    }

    private String ObjectListToString(List list) {
        String fullString = "";
        if (list != null) {
            for (Object object : list) {
                fullString += object.toString() + "\n\n";
            }
        }
        return fullString;
    }
}