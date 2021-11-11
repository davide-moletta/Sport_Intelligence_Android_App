package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    ImageView back;
    EditText search;
    TextView title;
    RecyclerView recycler;

    String[] searchInfo;
    List<String> recyclerData = new ArrayList<>();

    ButtonAdapter buttonAdapter;

    NavController navController;

    Neo4J neo4j;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        back = view.findViewById(R.id.back);
        search = view.findViewById(R.id.search);
        title = view.findViewById(R.id.title);
        recycler = view.findViewById(R.id.recycler);

        searchInfo = SearchFragmentArgs.fromBundle(getArguments()).getSearchInfo();

        if (searchInfo[0].equals("tournament")) {
            if (searchInfo[1].equals("noData")) {
                //RICERCA TORNEO

                neo4j = new Neo4J();
                title.setText(R.string.tournamentSearch);
                recyclerData = neo4j.fetchChampionships();
                neo4j.close();
            } else if (searchInfo[2].equals("noData")) {
                //RICERCA EDIZIONI

                neo4j = new Neo4J();
                title.setText(searchInfo[1]);
                recyclerData = neo4j.fetchTournamentEditions(searchInfo[1]);
                neo4j.close();
            }
        } else if (searchInfo[0].equals("athlete")) {
            if (searchInfo[1].equals("noData")) {
                //RICERCA GIOCATORI

                neo4j = new Neo4J();
                title.setText(R.string.athleteSearch);
                recyclerData = neo4j.fetchAthletes();
                neo4j.close();
            } else if (searchInfo[2].equals("noData")) {
                //RICERCA EDIZIONI PER GIOCATORE

                neo4j = new Neo4J();
                title.setText(searchInfo[1]);
                recyclerData = neo4j.fetchAthletesEditions(searchInfo[1]);
                neo4j.close();
            }
        }

        buttonAdapter = new ButtonAdapter(recyclerData, searchInfo, navController);
        recycler.setAdapter(buttonAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchedText = search.getText().toString();
                List<String> searchedList = new ArrayList<>();
                double maxSimilarity = 0.0;

                if (!searchedText.equals("")) {
                    for (String element : recyclerData) {
                        double similarity = similarity(element, searchedText);
                        if (similarity > maxSimilarity) {
                            maxSimilarity = similarity;
                        }
                    }

                    for (String element : recyclerData) {
                        if (similarity(searchedText, element) > maxSimilarity / 2) {
                            searchedList.add(element);
                        }
                    }

                    if (!searchedList.isEmpty()) {
                        recycler.setAdapter(new ButtonAdapter(searchedList, searchInfo, navController));
                        //buttonAdapter.setValues(searchedList);
                    }
                }else {
                    recycler.setAdapter(buttonAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back.setOnClickListener(v ->

        {
            if (!searchInfo[2].equals("noData")) {
                searchInfo[2] = "noData";
            } else if (!searchInfo[1].equals("noData")) {
                searchInfo[1] = "noData";
            }
            navController.navigateUp();
        });
    }

    private double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase(Locale.ROOT);
        s2 = s2.toLowerCase(Locale.ROOT);

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}