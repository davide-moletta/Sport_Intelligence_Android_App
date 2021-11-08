package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FilterCreatorFragment extends Fragment {

    private static final String FILE_NAME = "filters.txt";

    EditText editFilterName;
    TextView save;
    ImageView back;
    CheckBox checkMatchInfo, checkMatchStats, checkSetStats, checkSetHistory, checkQuotes;

    NavController navController;

    public FilterCreatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        editFilterName = view.findViewById(R.id.editFilterName);
        save = view.findViewById(R.id.saveButton);
        back = view.findViewById(R.id.back);
        checkMatchInfo = view.findViewById(R.id.checkMatchInfo);
        checkMatchStats = view.findViewById(R.id.checkMatchStats);
        checkSetStats = view.findViewById(R.id.checkSetStats);
        checkSetHistory = view.findViewById(R.id.checkSetHistory);
        checkQuotes = view.findViewById(R.id.checkQuotes);

        save.setOnClickListener(this::save);

        back.setOnClickListener(v -> navController.navigateUp());
    }

    private String filterBuilder() {
        String appliedFilters = "";

        if (checkMatchInfo.isChecked()) {
            appliedFilters += "matchInfo-";
        }
        if (checkMatchStats.isChecked()) {
            appliedFilters += "matchStats-";
        }
        if (checkSetStats.isChecked()) {
            appliedFilters += "setStats-";
        }
        if (checkSetHistory.isChecked()) {
            appliedFilters += "setHistory-";
        }
        if (checkQuotes.isChecked()) {
            appliedFilters += "quotes-";
        }

        appliedFilters = appliedFilters.substring(0, appliedFilters.length() - 1);

        return appliedFilters;
    }

    public void save(View v) {
        String filterName = editFilterName.getText().toString();
        String filterSelected = filterBuilder();

        if (filterName.equals("") || filterSelected.equals("")) {

            if (filterName.equals("")) {
                Toast.makeText(v.getContext(), R.string.noFilterTitle, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(v.getContext(), R.string.noFilterSelected, Toast.LENGTH_LONG).show();
            }
        } else {
            FileOutputStream fos = null;

            String fileData = read(v);
            String filter = filterName + ":" + filterSelected;
            String dataToWrite = fileData + filter;
            try {
                fos = v.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fos.write(dataToWrite.getBytes());

                clearAll();

                Toast.makeText(v.getContext(), R.string.savedFilter, Toast.LENGTH_LONG).show();
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

    public String read(View v) {
        FileInputStream fis = null;
        String fileData = "";

        try {
            fis = v.getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            fileData += sb.toString();

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
        return fileData;
    }

    private void clearAll() {
        editFilterName.getText().clear();
        checkMatchInfo.setChecked(false);
        checkMatchStats.setChecked(false);
        checkSetStats.setChecked(false);
        checkSetHistory.setChecked(false);
        checkQuotes.setChecked(false);
    }
}