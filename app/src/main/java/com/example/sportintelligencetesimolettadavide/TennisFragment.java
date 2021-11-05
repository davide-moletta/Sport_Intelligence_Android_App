package com.example.sportintelligencetesimolettadavide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TennisFragment extends Fragment {

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

        //Controller per gestire la navigazione tra i vari fragment
        NavController navController = Navigation.findNavController(view);

        //Ogni textView e imageView prende l'id dichiarato nel file xml
        ImageView back = view.findViewById(R.id.back);
        TextView tournamentSearch = view.findViewById(R.id.tournamentSearch);
        TextView athleteSearch = view.findViewById(R.id.athleteSearch);
        TextView filterCreation = view.findViewById(R.id.filterCreation);
        TextView FilterEditing = view.findViewById(R.id.filterEditing);
        TextView favouriteMatches = view.findViewById(R.id.favouriteMatches);

        //Se l'utente clicca sulla freccia viene rimosso il fragment attuale e si torna al fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());

        //Se l'utente clicca sugli altri pulsanti viene portato alle relative schermate
        tournamentSearch.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_tournamentSearchFragment));

        athleteSearch.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_athleteSearchFragment));

        filterCreation.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_filterCreatorFragment));

        FilterEditing.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_filterManagementFragment));

        favouriteMatches.setOnClickListener(v -> navController.navigate(R.id.action_tennisFragment_to_favouriteMatchesFragment));
    }
}