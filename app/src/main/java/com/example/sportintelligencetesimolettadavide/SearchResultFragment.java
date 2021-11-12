package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {
    private static final String FILE_FILTERS_NAME = "filters.txt";
    private static final String FILE_FAVOURITE_NAME = "favouriteMatches.txt";

    ImageView back, star;
    TextView tournamentName, tournamentInfo, firstPlayer, result, secondPlayer, duration, matchStatsLabel, setStatsLabel, setHistoryLabel, quotesLabel;
    Spinner filterSpinner, setSpinner;
    ConstraintLayout matchStatsLayout, setStatsLayout, setHistoryLayout, quotesLayout;

    boolean favourite = false;

    NavController navController;
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

        navController = Navigation.findNavController(view);
        fileFilters = new FileOperations(FILE_FILTERS_NAME, view);
        fileFavouriteMatches = new FileOperations(FILE_FAVOURITE_NAME, view);
        neo4J = new Neo4J();

        //Ottiene l'id del match selezionato
        int matchId = SearchResultFragmentArgs.fromBundle(getArguments()).getMatchId();

        back = view.findViewById(R.id.back);
        star = view.findViewById(R.id.star);
        tournamentName = view.findViewById(R.id.tournamentName);
        tournamentInfo = view.findViewById(R.id.tournamentInfo);
        firstPlayer = view.findViewById(R.id.firstPlayer);
        result = view.findViewById(R.id.result);
        secondPlayer = view.findViewById(R.id.secondPlayer);
        duration = view.findViewById(R.id.duration);
        filterSpinner = view.findViewById(R.id.spinnerFilter);
        setSpinner = view.findViewById(R.id.setSpinner);
        matchStatsLabel = view.findViewById(R.id.matchStatsLabel);
        setStatsLabel = view.findViewById(R.id.setStatsLabel);
        setHistoryLabel = view.findViewById(R.id.setHistoryLabel);
        quotesLabel = view.findViewById(R.id.quotesLabel);
        matchStatsLayout = view.findViewById(R.id.matchStats);
        setStatsLayout = view.findViewById(R.id.setStats);
        setHistoryLayout = view.findViewById(R.id.setHistory);
        quotesLayout = view.findViewById(R.id.matchQuotes);

        //Carica da file i filtri presenti e ne inserisce i valori nello spinner
        String fileFiltersString = fileFilters.load();
        String[] filters = fileFiltersString.split("\n");
        String[] spinnerFilters = new String[filters.length + 1];
        spinnerFilters[0] = "SELEZIONA IL FILTRO";
        for (int i = 0; i < filters.length; i++) {
            spinnerFilters[i + 1] = filters[i].split(":")[0];
        }
        //Imposta i valori trovati come adapter per lo spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, spinnerFilters);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        //Controlla se il match è tra i preferiti o no e in base al risultato imposta la stella come piena o vuota
        String[] favouriteMatches = fileFavouriteMatches.load().split("\n");
        for (String favouriteMatch : favouriteMatches) {
            if (favouriteMatch.equals(String.valueOf(matchId))) {
                star.setBackgroundResource(R.drawable.ic_fullstar);
                favourite = true;
            }
        }

        //Ottiene i dati del match dal database
        match = neo4J.fetchAllDataFromId(matchId);

        String editionAndDate = neo4J.fetchEditionFromId(matchId) + " - " + match.getDate();
        String tournamentInfoString = match.getLocation() + ", " + match.getField() + " - " + match.getRound();
        List<String> setGames = new ArrayList<>(), setFifteens = new ArrayList<>(), setStat = new ArrayList<>(), setTiebreaks = new ArrayList<>();

        matchStat = match.getMatchStats();
        quotes = match.getQuotes();
        setsStat = match.getSetsStats();
        setsFifteens = match.getSetsFifteens();
        setsHistory = match.getSetsHistory();
        setsTiebreaks = match.getSetsTiebreaks();

        if (ObjectListToString(matchStat).equals("")) {
            matchStatsLayout.setVisibility(View.INVISIBLE);
        } else {
            matchStatsLabel.setText(ObjectListToString(matchStat));
        }

        if (ObjectListToString(quotes).equals("")) {
            quotesLabel.setVisibility(View.INVISIBLE);
        } else {
            quotesLabel.setText(ObjectListToString(quotes));
        }

        for (List list : setsStat) {
            setStat.add(ObjectListToString(list));
        }
        for (List list : setsHistory) {
            setGames.add(ObjectListToString(list));
        }
        for (List list : setsFifteens) {
            setFifteens.add(ObjectListToString(list));
        }
        for (List list : setsTiebreaks) {
            setTiebreaks.add(ObjectListToString(list));
        }


        //CODICE PER MOSTRARE I DATI DEI SET


        //Calcola il numero di set e crea il vettore da inserire nell'adapter
        int numberOfSets = 0;
        for (String set : setGames) {
            if (!set.equals("")) {
                numberOfSets++;
            }
        }

        String[] sets = new String[numberOfSets];
        for (int i = 0; i < numberOfSets; i++) {
            int j = i + 1;
            sets[i] = "Set " + j;
        }
        //Inserisce il vettore creato in un adapter e lo assegna allo spinner dei set
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, sets);
        setAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinner.setAdapter(setAdapter);

        tournamentName.setText(editionAndDate);
        tournamentInfo.setText(tournamentInfoString);
        firstPlayer.setText(match.getFirstPlayer());
        result.setText(match.getResult());
        secondPlayer.setText(match.getSecondPlayer());
        duration.setText(match.getDuration());

        neo4J.close();

        //Imposta on Listener sullo spinner dei filtri per controllare quando viene selezionato un elemento diverso
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Imposta on Listener sullo spinner dei set per controllare quando viene selezionato un elemento diverso
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Imposta un OnClickListener sulla stella in modo da poter rendere preferito il match attuale o di rimuoverlo dai preferiti
        star.setOnClickListener(v -> {
            if (favourite) {
                fileFavouriteMatches.searchAndDelete(String.valueOf(matchId), fileFavouriteMatches.load());
                star.setBackgroundResource(R.drawable.ic_emptystar);
                favourite = false;
            } else {
                fileFavouriteMatches.save(String.valueOf(matchId));
                star.setBackgroundResource(R.drawable.ic_fullstar);
                favourite = true;
            }
        });

        //Imposta un OnClickListener per permettere la navigazione verso il fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());
    }

    //Trasforma una lista di oggetti in una stringa da inserire poi nelle label
    private String ObjectListToString(List list) {
        StringBuilder fullString = new StringBuilder();
        if (list != null) {
            for (Object object : list) {
                fullString.append(object.toString()).append("\n\n");
            }
        }
        if (!fullString.toString().equals("")) {
            fullString = new StringBuilder(fullString.substring(0, fullString.length() - 2));
        }
        return fullString.toString();
    }
}