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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterManagementFragment extends Fragment {
    private static final String FILE_NAME = "filters.txt";

    ImageView back;
    TextView deleteAll;
    List<String> fileRows = new ArrayList<>();

    SavedFilterAdapter filterAdapter;
    RecyclerView recycler;

    FileOperations fileOperations;

    NavController navController;

    public FilterManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        back = view.findViewById(R.id.back);
        deleteAll = view.findViewById(R.id.deleteAll);
        recycler = view.findViewById(R.id.recycler);

        //Crea un oggetto FileOperations per la scrittura e lettura su file di testo
        fileOperations = new FileOperations(FILE_NAME, view);

        //Legge da file tutti i filtri salvati e li inserisce in una lista
        Collections.addAll(fileRows, fileOperations.load().split("\n"));

        //Controlla se sono presenti filtri da mostrare
        if (fileRows.get(0).equals("")) {
            //FILTRI NON PRESENTI
            Toast.makeText(view.getContext(), R.string.noFilters, Toast.LENGTH_SHORT).show();
        } else {
            //FILTRI PRESENTI
            //Se trova dei filtri li passa ad un SavedFilterAdapter per poi inserirli nella recyclerView
            filterAdapter = new SavedFilterAdapter(fileRows, navController, fileOperations.load());
            recycler.setAdapter(filterAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        //Imposta un OnClickListener sul pulsante elimina tutti per l'eliminazione di tutti i filtri salvati
        deleteAll.setOnClickListener(v -> {
            fileOperations.clearFile();
            Toast.makeText(v.getContext(), R.string.allFilterDeleted, Toast.LENGTH_SHORT).show();
            //Pulisce la lista dei filtri e ricarica la recyclerView aggiornata
            fileRows.clear();
            filterAdapter = new SavedFilterAdapter(fileRows, navController, fileOperations.load());
            recycler.setAdapter(filterAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        });

        //Imposta un OnClickListener sulla freccia per permettere la navigazione verso il fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());
    }
}