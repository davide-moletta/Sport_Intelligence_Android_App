package com.example.sportintelligencetesimolettadavide;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sportintelligencetesimolettadavide.MainActivity.neo4J;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteMatchesFragment extends Fragment {
    private static final String FILE_NAME = "favouriteMatches.txt";
    ImageView back;
    RecyclerView recycler;

    FileOperations fileOperations;
    FireBase fireBase;

    List<String> fileRows = new ArrayList<>();

    SharedPreferences sharedPreferences;

    FavouriteMatchAdapter favouriteMatchAdapter;

    NavController navController;

    public FavouriteMatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_matches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = this.requireActivity().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        fireBase = new FireBase(view, this);
        navController = Navigation.findNavController(view);
        fileOperations = new FileOperations(FILE_NAME, view);

        back = view.findViewById(R.id.back);
        recycler = view.findViewById(R.id.recycler);

        //Prende tutti i match preferiti dal file di testo relativo e li inserisce in una lista
        Collections.addAll(fileRows, fileOperations.load().split("\n"));

        //Controlla se esistono dei match preferiti da mostrare
        if (fileRows.get(0).equals("")) {
            //MATCH NON TROVATI
            Toast.makeText(view.getContext(), R.string.noFavouriteMatches, Toast.LENGTH_SHORT).show();
        } else {
            //MATCH TROVATI
            //Se trova dei match preferiti crea un FavouriteMatchAdapter e lo assegna alla recyclerView per la visualizzazione
            favouriteMatchAdapter = new FavouriteMatchAdapter(fileRows, navController, neo4J, fileOperations.load(), fireBase, sharedPreferences);
            recycler.setAdapter(favouriteMatchAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        //Imposta un OnClickListener sulla freccia per la chiusura della comunicazione col database e per la navigazione verso il fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());
    }
}