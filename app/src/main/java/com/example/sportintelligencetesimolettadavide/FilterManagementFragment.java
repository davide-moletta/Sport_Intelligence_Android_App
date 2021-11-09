package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

        Collections.addAll(fileRows, load(view).split("\n"));

        if (fileRows.get(0).equals("")) {
            Toast.makeText(view.getContext(), R.string.noFilters, Toast.LENGTH_LONG).show();
        } else {
            filterAdapter = new SavedFilterAdapter(fileRows, navController, load(view));
            recycler.setAdapter(filterAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        deleteAll.setOnClickListener(v -> {
            clearFile(v);
            fileRows.clear();
            filterAdapter = new SavedFilterAdapter(fileRows, navController, load(v));
            recycler.setAdapter(filterAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        });

        back.setOnClickListener(v -> navController.navigateUp());
    }

    public String load(View v) {
        FileInputStream fis = null;
        String filters = "";

        try {
            fis = v.getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            filters += sb.toString();

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
        return filters;
    }

    public void clearFile(View v) {
        FileOutputStream fos = null;

        try {
            fos = v.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write("".getBytes());

            Toast.makeText(v.getContext(), R.string.allFilterDeleted, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}