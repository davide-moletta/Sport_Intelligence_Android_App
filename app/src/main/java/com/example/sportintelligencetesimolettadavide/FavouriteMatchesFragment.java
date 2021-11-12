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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteMatchesFragment extends Fragment {
    private static final String FILE_NAME = "favouriteMatches.txt";

    ImageView back;
    RecyclerView recycler;
    FileOperations fileOperations;

    List<String> fileRows = new ArrayList<>();

    FavouriteMatchAdapter favouriteMatchAdapter;

    Neo4J neo4j;
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

        navController = Navigation.findNavController(view);
        neo4j = new Neo4J();

        fileOperations = new FileOperations(FILE_NAME, view);
        back = view.findViewById(R.id.back);
        recycler = view.findViewById(R.id.recycler);

        System.out.println(fileOperations.load());

        Collections.addAll(fileRows, fileOperations.load().split("\n"));

        if (fileRows.get(0).equals("")) {
            Toast.makeText(view.getContext(), R.string.noFavouriteMatches, Toast.LENGTH_LONG).show();
        } else {
            favouriteMatchAdapter = new FavouriteMatchAdapter(fileRows, navController, neo4j, fileOperations.load());
            recycler.setAdapter(favouriteMatchAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        back.setOnClickListener(v -> {
            navController.navigateUp();
            neo4j.close();
        });
    }
}