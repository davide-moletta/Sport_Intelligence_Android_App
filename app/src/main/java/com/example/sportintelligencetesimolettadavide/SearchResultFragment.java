package com.example.sportintelligencetesimolettadavide;

import static com.example.sportintelligencetesimolettadavide.MainActivity.neo4J;

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
    TextView tournamentName, tournamentInfo, firstPlayer, result, secondPlayer, duration, subTitleSetStats, subTitleHistory,
            matchStatsLabel, setStatsLabel, setHistoryLabel, quotesLabel;
    Spinner filterSpinner, setSpinner;
    ConstraintLayout matchInfoLayout, matchStatsLayout, setStatsLayout, setHistoryLayout, quotesLayout;

    boolean favourite = false, matchStatsInfo = true, setStatInfo = true, setHistoryInfo = true, quotesInfo = true;

    NavController navController;
    FileOperations fileFilters, fileFavouriteMatches;
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

        //Ottiene l'id del match selezionato
        int matchId = SearchResultFragmentArgs.fromBundle(getArguments()).getMatchId();

        //Ottiene l'id degli elementi del file xml
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
        subTitleHistory = view.findViewById(R.id.subTitleHistory);
        subTitleSetStats = view.findViewById(R.id.subTitleSetStats);
        matchStatsLabel = view.findViewById(R.id.matchStatsLabel);
        setStatsLabel = view.findViewById(R.id.setStatsLabel);
        setHistoryLabel = view.findViewById(R.id.setHistoryLabel);
        quotesLabel = view.findViewById(R.id.quotesLabel);
        matchInfoLayout = view.findViewById(R.id.matchInfo);
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
        if (!favourite) {
            star.setBackgroundResource(R.drawable.ic_emptystar);
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

        //Salva le informazioni dei set in un vettore per la corretta visualizzazione dei dati tramite spinner
        String[] stat = setListToString(setStat, numberOfSets);
        String[] history = setHistoryStringBuilder(setGames, setFifteens, setTiebreaks, numberOfSets);

        //Controlla se sono presenti dati ricevuti dal database (per match vecchi i dati non vengono salvati)
        //se non sono presenti dati nasconde le relative finestre
        if (ObjectListToString(matchStat).equals("")) {
            matchStatsInfo = false;
            matchStatsLayout.setVisibility(View.INVISIBLE);
        } else {
            matchStatsLabel.setText(ObjectListToString(matchStat));
        }

        if (ObjectListToString(quotes).equals("no data")) {
            quotesInfo = false;
            quotesLayout.setVisibility(View.INVISIBLE);
        } else {
            quotesLabel.setText(ObjectListToString(quotes));
        }

        if (stat.length == 0) {
            setStatInfo = false;
            setStatsLayout.setVisibility(View.INVISIBLE);
        }

        if (history.length == 0) {
            setHistoryInfo = false;
            setHistoryLayout.setVisibility(View.INVISIBLE);
        }

        if (!setStatInfo && !setHistoryInfo) {
            setSpinner.setVisibility(View.INVISIBLE);
        }

        //Assegna i dati raccolti alle verie textView
        tournamentName.setText(editionAndDate);
        tournamentInfo.setText(tournamentInfoString);
        firstPlayer.setText(match.getFirstPlayer());
        result.setText(match.getResult());
        secondPlayer.setText(match.getSecondPlayer());
        duration.setText(match.getDuration());

        //Imposta on Listener sullo spinner dei filtri per controllare quando viene selezionato un elemento diverso
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //In base al filtro selezionato rimuove o aggiunge i campi precedentemente selezionati
                if (i == 0) {
                    matchInfoLayout.setVisibility(View.VISIBLE);

                    if (matchStatsInfo) {
                        matchStatsLayout.setVisibility(View.VISIBLE);
                    }
                    if (setStatInfo) {
                        setStatsLayout.setVisibility(View.VISIBLE);
                    }
                    if (setHistoryInfo) {
                        setHistoryLayout.setVisibility(View.VISIBLE);
                    }
                    if (quotesInfo) {
                        quotesLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    String filter = filters[i - 1];
                    matchInfoLayout.setVisibility(View.INVISIBLE);
                    matchStatsLayout.setVisibility(View.INVISIBLE);
                    setStatsLayout.setVisibility(View.INVISIBLE);
                    setHistoryLayout.setVisibility(View.INVISIBLE);
                    quotesLayout.setVisibility(View.INVISIBLE);

                    String[] filterTypes = filter.split(":")[1].split("-");

                    for (String type : filterTypes) {
                        if (type.equals("matchInfo")) {
                            matchInfoLayout.setVisibility(View.VISIBLE);
                        }
                        if (type.equals("matchStats") && matchStatsInfo) {
                            matchStatsLayout.setVisibility(View.VISIBLE);
                        }
                        if (type.equals("setStats") && setStatInfo) {
                            setStatsLayout.setVisibility(View.VISIBLE);
                        }
                        if (type.equals("setHistory") && setHistoryInfo) {
                            setHistoryLayout.setVisibility(View.VISIBLE);
                        }
                        if (type.equals("quotes") && quotesInfo) {
                            quotesLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Imposta on Listener sullo spinner dei set per controllare quando viene selezionato un elemento diverso
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //In base al set selezionato aggiorna le informazioni nelle rispettive textview
                if (!setStatInfo || !setHistoryInfo) {
                    if (setStatInfo) {
                        int j = i + 1;
                        String statSubTitle = "STATISTICHE SET " + j;
                        subTitleSetStats.setText(statSubTitle);
                        setStatsLabel.setText(stat[i]);
                    } else if (setHistoryInfo) {
                        int j = i + 1;
                        String historySubTitle = "STORICO SET " + j;
                        subTitleHistory.setText(historySubTitle);
                        setHistoryLabel.setText(history[i]);
                    }
                } else {
                    int j = i + 1;
                    String statSubTitle = "STATISTICHE SET " + j;
                    String historySubTitle = "STORICO SET " + j;

                    subTitleSetStats.setText(statSubTitle);
                    subTitleHistory.setText(historySubTitle);

                    setStatsLabel.setText(stat[i]);
                    setHistoryLabel.setText(history[i]);
                }

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

    private String[] setListToString(List<String> listToConvert, int setNumber) {
        String[] sets = new String[setNumber];
        StringBuilder fullString;

        for (int i = 0; i < setNumber; i++) {
            fullString = new StringBuilder();
            if (!listToConvert.get(i).equals("")) {
                String[] rows = listToConvert.get(i).split("\n");
                for (String row : rows) {
                    fullString.append(row).append("\n");
                }
                if (!fullString.toString().equals("")) {
                    fullString = new StringBuilder(fullString.substring(0, fullString.length() - 2));
                }
                sets[i] = fullString.toString();
            }
        }

        return sets;
    }

    private String[] setHistoryStringBuilder(List<String> games, List<String> fifteens, List<String> tiebreaks, int setNumber) {
        String[] sets = new String[setNumber];
        StringBuilder fullString;

        for (int i = 0; i < setNumber; i++) {
            fullString = new StringBuilder();
            if (!games.get(i).equals("") && !fifteens.get(i).equals("")) {
                String[] setGames = games.get(i).split("\n");
                String[] setFifteens = fifteens.get(i).split("\n");


                for (int j = 0; j < setFifteens.length; j++) {
                    fullString.append("\n").append(setGames[j]).append("\n").append(setFifteens[j]);
                }
                if (setGames.length > setFifteens.length) {
                    fullString.append("\n\n").append(setGames[setGames.length - 1]);
                }
                fullString.append("\n");

                if (!tiebreaks.get(i).equals("no data")) {
                    fullString.append("\n").append("TIEBREAK").append("\n\n");

                    String[] setTiebreaks = tiebreaks.get(i).split("\n");
                    for (String setTiebreak : setTiebreaks) {
                        fullString.append(setTiebreak).append("\n");
                    }
                }
            }
            if (!fullString.toString().equals("")) {
                fullString = new StringBuilder(fullString.substring(1, fullString.length() - 1));
            }
            sets[i] = fullString.toString();
        }
        return sets;
    }
}