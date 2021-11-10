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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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