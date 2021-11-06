package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class TournamentSearchFragment extends Fragment {

    public TournamentSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] searchInfo;
        List<String> recyclerData = new ArrayList<>();
        Neo4J neo4j = new Neo4J();
        ButtonAdapter buttonAdapter;

        NavController navController = Navigation.findNavController(view);

        ImageView back = view.findViewById(R.id.back);
        EditText search = view.findViewById(R.id.search);
        RecyclerView recycler = view.findViewById(R.id.recycler);

        searchInfo = TournamentSearchFragmentArgs.fromBundle(getArguments()).getSearchInfo();

        if (searchInfo[0].equals("tournament")) {
            if (searchInfo[1].equals("noData")) {
                //RICERCA TORNEO

                recyclerData = neo4j.fetchChampionships();
                neo4j.close();
            } else if (searchInfo[2].equals("noData")) {
                //RICERCA EDIZIONI

                recyclerData = neo4j.fetchTournamentEditions(searchInfo[1]);
                neo4j.close();
            }
        } else if (searchInfo[0].equals("athlete")) {
            if (searchInfo[1].equals("noData")) {
                //RICERCA GIOCATORI

                recyclerData = neo4j.fetchAthletes();
                neo4j.close();
            } else if (searchInfo[2].equals("noData")) {
                //RICERCA EDIZIONI PER GIOCATORE

                recyclerData = neo4j.fetchAthletesEditions(searchInfo[1]);
                neo4j.close();
            }
        }

        buttonAdapter = new ButtonAdapter(recyclerData, searchInfo, navController);
        recycler.setAdapter(buttonAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        back.setOnClickListener(v -> {
            if (!searchInfo[2].equals("noData")) {
                searchInfo[2] = "noData";
            } else if (!searchInfo[1].equals("noData")) {
                searchInfo[1] = "noData";
            }
            navController.navigateUp();
        });
    }
}

/*
else if (searchInfo[0].equals("athlete")) {
            if (searchInfo[1].equals("noData")) {
                //RICERCA GIOCATORI

                recyclerData = neo4j.fetchAthletes();
                neo4j.close();
            } else if (searchInfo[2].equals("noData")) {
                //RICERCA EDIZIONI PER GIOCATORE

                recyclerData = neo4j.fetchAthletesEditions();
                neo4j.close();
            } else {
                //RICERCA MATCH PER GIOCATORE

                List<Match> recyclerMatches = neo4j.fetchTournamentMatches(searchInfo[2]);
                neo4j.close();

                MatchAdapter matchAdapter = new MatchAdapter(recyclerMatches, navController);
                recycler.setAdapter(matchAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
            }

            buttonAdapter = new ButtonAdapter(recyclerData, searchInfo, navController);
            recycler.setAdapter(buttonAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }
 */