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

        List<String> championships;

        NavController navController = Navigation.findNavController(view);

        ImageView back = view.findViewById(R.id.back);
        EditText search = view.findViewById(R.id.search);
        RecyclerView recycler = view.findViewById(R.id.recycler);


        Neo4J neo4j = new Neo4J("bolt://192.168.1.5:7687", "neo4j", "admin");
        championships = neo4j.fetchChampionships();
        neo4j.close();

        MatchAdapter adapter = new MatchAdapter(championships);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        back.setOnClickListener(v -> navController.navigateUp());
    }
}