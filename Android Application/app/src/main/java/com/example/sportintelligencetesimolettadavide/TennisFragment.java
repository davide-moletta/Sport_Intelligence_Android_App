package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TennisFragment extends Fragment {

    ImageView back;
    TextView tournamentSearch, athleteSearch, filterCreation, FilterEditing, favouriteMatches;

    NavController navController;

    public TennisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tennis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] searchInfo = new String[3];

        //Controller per gestire la navigazione tra i vari fragment
        navController = Navigation.findNavController(view);

        //Ogni textView e imageView prende l'id dichiarato nel file xml
        back = view.findViewById(R.id.back);
        tournamentSearch = view.findViewById(R.id.tournamentSearch);
        athleteSearch = view.findViewById(R.id.athleteSearch);
        filterCreation = view.findViewById(R.id.filterCreation);
        FilterEditing = view.findViewById(R.id.filterEditing);
        favouriteMatches = view.findViewById(R.id.favouriteMatches);

        //Se l'utente clicca sulla freccia viene rimosso il fragment attuale e si torna al fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());

        //Se l'utente clicca sugli altri pulsanti viene portato alle relative schermate
        //Il vettore searchInfo viene passato di fragment in fragment e permette di capire a che punto della ricerca si è
        tournamentSearch.setOnClickListener(v -> {
            searchInfo[0] = "tournament";
            searchInfo[1] = "noData";
            searchInfo[2] = "noData";
            NavDirections action = TennisFragmentDirections.actionTennisFragmentToTournamentSearchFragment(searchInfo);
            navController.navigate(action);
        });

        athleteSearch.setOnClickListener(v -> {
            searchInfo[0] = "athlete";
            searchInfo[1] = "noData";
            searchInfo[2] = "noData";
            NavDirections action = TennisFragmentDirections.actionTennisFragmentToTournamentSearchFragment(searchInfo);
            navController.navigate(action);
        });

        filterCreation.setOnClickListener(v -> {
            NavDirections action = TennisFragmentDirections.actionTennisFragmentToFilterCreatorFragment("noEdit");
            navController.navigate(action);

        });

        FilterEditing.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_filterManagementFragment));

        favouriteMatches.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_favouriteMatchesFragment));
    }
}