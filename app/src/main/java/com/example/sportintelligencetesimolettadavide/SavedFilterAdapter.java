package com.example.sportintelligencetesimolettadavide;

import static com.example.sportintelligencetesimolettadavide.MainActivity.TELEGRAM_CHAT_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedFilterAdapter extends RecyclerView.Adapter<SavedFilterAdapter.ViewHolder> {
    private static final String FILE_NAME = "filters.txt";

    private final List<String> filters;
    private final NavController navController;

    TextView filterName;
    ImageView edit, delete;

    String fileData;

    SharedPreferences sharedPreferences;
    FireBase fireBase;
    FileOperations fileOperations;

    public SavedFilterAdapter(List<String> filters, NavController navController, String fileData,
                              FireBase fireBase, SharedPreferences sharedPreferences) {
        this.filters = filters;
        this.navController = navController;
        this.fileData = fileData;
        this.fireBase = fireBase;
        this.sharedPreferences = sharedPreferences;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_saved_filter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fileFilter = filters.get(position);

        //Ottiene il nome del filtro
        String filterNameString = fileFilter.split(":")[0];

        //Crea un oggetto FileOperations per la scrittura e lettura su file di testo
        fileOperations = new FileOperations(FILE_NAME, holder.itemView);

        filterName = holder.filterName;
        filterName.setText(filterNameString);

        edit = holder.edit;
        delete = holder.delete;

        //Imposta un OnClickListener per permettere la navigazione verso il fragment per la modifica del filtro selezionato
        edit.setOnClickListener(v -> {
            NavDirections action = FilterManagementFragmentDirections.actionFilterManagementFragmentToFilterCreatorFragment(fileFilter);
            //Pulisce la lista dei filtri per aggiornarla
            filters.clear();
            navController.navigate(action);

        });

        //Imposta un OnClickListener per permettere l'eliminazione del filtro selezionato
        delete.setOnClickListener(v -> {
            //Creca ed elimina il filtro selezionato dal file di testo
            String newFileData = fileOperations.searchAndDelete(fileFilter, fileData);
            Toast.makeText(v.getContext(), R.string.deleteFilter, Toast.LENGTH_SHORT).show();
            //Aggiorna la lista e la stringa contenente i filtri trovati nel file
            setFileData(newFileData);
            filters.remove(holder.getAbsoluteAdapterPosition());
            this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            if (!sharedPreferences.getString(TELEGRAM_CHAT_ID, "").equals("no ID")) {
                fireBase.updateUser();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView filterName;
        private final ImageView edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            filterName = itemView.findViewById(R.id.filterName);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}