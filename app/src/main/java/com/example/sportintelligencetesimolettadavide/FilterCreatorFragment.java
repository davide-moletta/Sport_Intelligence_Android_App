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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FilterCreatorFragment extends Fragment {

    private static final String FILE_NAME = "filters.txt";

    EditText editFilterName;
    TextView save;
    ImageView back;
    CheckBox checkMatchInfo, checkMatchStats, checkSetStats, checkSetHistory, checkQuotes;

    NavController navController;

    FileOperations fileOperations;

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
        String editFilter = FilterCreatorFragmentArgs.fromBundle(getArguments()).getEditFilter();

        fileOperations = new FileOperations(FILE_NAME, view);

        editFilterName = view.findViewById(R.id.editFilterName);
        save = view.findViewById(R.id.saveButton);
        back = view.findViewById(R.id.back);
        checkMatchInfo = view.findViewById(R.id.checkMatchInfo);
        checkMatchStats = view.findViewById(R.id.checkMatchStats);
        checkSetStats = view.findViewById(R.id.checkSetStats);
        checkSetHistory = view.findViewById(R.id.checkSetHistory);
        checkQuotes = view.findViewById(R.id.checkQuotes);

        if (editFilter.equals("noEdit")) {
            save.setOnClickListener(v -> {
                String filterName = editFilterName.getText().toString();
                String filterSelected = filterBuilder();

                if (filterName.equals("") || filterSelected.equals("")) {

                    if (filterName.equals("")) {
                        Toast.makeText(v.getContext(), R.string.noFilterTitle, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), R.string.noFilterSelected, Toast.LENGTH_LONG).show();
                    }
                } else {
                    String filter = filterName + ":" + filterSelected;
                    fileOperations.save(filter);
                    clearAll();
                    Toast.makeText(view.getContext(), R.string.savedFilter, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            setUpFilter(editFilter);
            save.setOnClickListener(v -> {
                String newFilterName = editFilterName.getText().toString();
                String filterSelected = filterBuilder();
                String newFilter = newFilterName + ":" + filterSelected;

                if (newFilterName.equals("") || filterSelected.equals("")) {

                    if (newFilterName.equals("")) {
                        Toast.makeText(v.getContext(), R.string.noFilterTitle, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), R.string.noFilterSelected, Toast.LENGTH_LONG).show();
                    }
                } else {
                    fileOperations.replaceFilter(editFilter, newFilter);
                    Toast.makeText(v.getContext(), R.string.updateFilter, Toast.LENGTH_LONG).show();
                    clearAll();
                }
            });
        }

        back.setOnClickListener(v -> navController.navigateUp());
    }

    private void setUpFilter(String editFilter) {
        editFilterName.setText(editFilter.split(":")[0]);

        String[] filterTypes = editFilter.split(":")[1].split("-");

        for (String filterType : filterTypes) {
            if (filterType.equals("matchInfo")) {
                checkMatchInfo.setChecked(true);
            }
            if (filterType.equals("matchStats")) {
                checkMatchStats.setChecked(true);
            }
            if (filterType.equals("setStats")) {
                checkSetStats.setChecked(true);
            }
            if (filterType.equals("setHistory")) {
                checkSetHistory.setChecked(true);
            }
            if (filterType.equals("quotes")) {
                checkQuotes.setChecked(true);
            }
        }
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

        if (!appliedFilters.equals("")) {
            appliedFilters = appliedFilters.substring(0, appliedFilters.length() - 1);
        }

        return appliedFilters;
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