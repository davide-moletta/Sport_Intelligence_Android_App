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

import java.util.List;

public class MatchSelectorFragment extends Fragment {

    public MatchSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] searchInfo;
        Neo4J neo4j = new Neo4J();
        List<Match> recyclerMatches;

        NavController navController = Navigation.findNavController(view);

        RecyclerView recycler = view.findViewById(R.id.recycler);

        searchInfo = MatchSelectorFragmentArgs.fromBundle(getArguments()).getSearchInfo();

        if (searchInfo[0].equals("tournament")) {
            //RICERCA MATCH PER TORNEO

            recyclerMatches = neo4j.fetchTournamentMatches(searchInfo[2]);
            neo4j.close();
        } else {
            //RICERCA MATCH PER GIOCATORE O EDIZIONE
            recyclerMatches = neo4j.fetchAthletesMatches(searchInfo[1], searchInfo[2]);
            neo4j.close();
        }

        MatchAdapter matchAdapter = new MatchAdapter(recyclerMatches, navController);
        recycler.setAdapter(matchAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}