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
        //Ottiene il valore ricevuto dal fragment precedente che segnala se si sta creando un filtro o se si sta modificando un filtro esistente
        String editFilter = FilterCreatorFragmentArgs.fromBundle(getArguments()).getEditFilter();

        //Crea un elemento FileOperations per permettere la scrittura e lettura su file di testo
        fileOperations = new FileOperations(FILE_NAME, view);

        editFilterName = view.findViewById(R.id.editFilterName);
        save = view.findViewById(R.id.saveButton);
        back = view.findViewById(R.id.back);
        checkMatchInfo = view.findViewById(R.id.checkMatchInfo);
        checkMatchStats = view.findViewById(R.id.checkMatchStats);
        checkSetStats = view.findViewById(R.id.checkSetStats);
        checkSetHistory = view.findViewById(R.id.checkSetHistory);
        checkQuotes = view.findViewById(R.id.checkQuotes);

        //Controlla se il filtro è da aggiornare o da creare
        if (editFilter.equals("noEdit")) {
            //FILTRO DA CREARE
            //Imposta un OnClickListener che una volta premuto su salva prende il nome del filtro e i valori selezionati per poi salvari sul file di testo
            save.setOnClickListener(v -> {
                String filterName = editFilterName.getText().toString();
                String filterSelected = filterBuilder();

                //Controlla se il nome del filtro non è vuoto e se è stato selezionato almeno un filtro
                if (filterName.equals("") || filterSelected.equals("")) {
                    if (filterName.equals("")) {
                        Toast.makeText(v.getContext(), R.string.noFilterTitle, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), R.string.noFilterSelected, Toast.LENGTH_LONG).show();
                    }
                } else {
                    String filter = filterName + ":" + filterSelected;
                    fileOperations.save(filter);
                    Toast.makeText(view.getContext(), R.string.savedFilter, Toast.LENGTH_LONG).show();
                    clearAll();
                }
            });
        } else {
            //FILTRO DA MODIFICARE
            //Aggiorna la schermata con i dati relativi al filtro che si intende modificare
            setUpFilter(editFilter);

            //Imposta un OnClickListener che una volta premuto su salva prende il nome del filtro e i valori selezionati per poi salvari sul file di testo
            //dopo aver eliminato il filtro che andava modificato
            save.setOnClickListener(v -> {
                String newFilterName = editFilterName.getText().toString();
                String filterSelected = filterBuilder();
                String newFilter = newFilterName + ":" + filterSelected;

                //Controlla se il nome del filtro non è vuoto e se è stato selezionato almeno un filtro
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

        //Imposta un OnClickListener per la navigazione verso il fragment precedente
        back.setOnClickListener(v -> navController.navigateUp());
    }

    //In base al filtro ricevuto imposta i campi della pagina come il filtro segnala
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

    //In base ai filtri selezionati dall'utente crea una stringa da inserire nel file di testo
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

    //Pulisce tutti i campi della pagina
    private void clearAll() {
        editFilterName.getText().clear();
        checkMatchInfo.setChecked(false);
        checkMatchStats.setChecked(false);
        checkSetStats.setChecked(false);
        checkSetHistory.setChecked(false);
        checkQuotes.setChecked(false);
    }
}