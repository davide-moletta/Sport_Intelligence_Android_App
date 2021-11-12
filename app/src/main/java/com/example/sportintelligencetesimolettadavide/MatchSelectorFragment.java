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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MatchSelectorFragment extends Fragment {

    String[] searchInfo;
    Neo4J neo4j;
    List<Match> recyclerMatches;

    NavController navController;

    TextView title;
    ImageView back;
    RecyclerView recycler;

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

        neo4j = new Neo4J();

        navController = Navigation.findNavController(view);

        title = view.findViewById(R.id.title);
        back = view.findViewById(R.id.back);
        recycler = view.findViewById(R.id.recycler);

        //Prende il valore del vettore searchInfo arrivato del fragment SearchFragment
        searchInfo = MatchSelectorFragmentArgs.fromBundle(getArguments()).getSearchInfo();

        //Controlla se i match da cercare nel database sono per torneo o giocatore e li ricerca
        if (searchInfo[0].equals("tournament")) {
            //RICERCA MATCH PER TORNEO E EDIZIONE

            title.setText(searchInfo[2]);
            recyclerMatches = neo4j.fetchTournamentMatches(searchInfo[2]);
        } else {
            //RICERCA MATCH PER GIOCATORE E EDIZIONE

            title.setText(searchInfo[1]);
            recyclerMatches = neo4j.fetchAthletesMatches(searchInfo[1], searchInfo[2]);
        }

        //Imposta l'Adapter per la recyclerView cosi da permettere la visualizzazione degli elementi
        MatchAdapter matchAdapter = new MatchAdapter(recyclerMatches, navController);
        recycler.setAdapter(matchAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Imposta un OnClickListenere sulla freccia per chiudera la comunicazione col database, aggiornare il vettore searchInfo e tornare al fragmnent precedente
        back.setOnClickListener(v -> {
            searchInfo[2] = "noData";
            neo4j.close();
            navController.navigateUp();
        });
    }
}