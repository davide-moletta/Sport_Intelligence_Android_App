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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class FavouriteMatchesFragment extends Fragment {
    private static final String FILE_NAME = "favouriteMatches.txt";

    ImageView back;
    RecyclerView recycler;

    List<String> fileRows;

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

        navController = Navigation.findNavController(view);
        back = view.findViewById(R.id.back);
        recycler = view.findViewById(R.id.recycler);

        Collections.addAll(fileRows, load(view).split("\n"));

        if (fileRows.get(0).equals("")) {
            Toast.makeText(view.getContext(), R.string.noFavouriteMatches, Toast.LENGTH_LONG).show();
        } else {
            favouriteMatchAdapter = new FavouriteMatchAdapter(fileRows, navController);
            recycler.setAdapter(favouriteMatchAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        back.setOnClickListener(v -> navController.navigateUp());
    }

    public String load(View v) {
        FileInputStream fis = null;
        String matchIds = "";

        try {
            fis = v.getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            matchIds += sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return matchIds;
    }
}