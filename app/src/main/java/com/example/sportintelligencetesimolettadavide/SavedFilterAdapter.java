package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SavedFilterAdapter extends RecyclerView.Adapter<SavedFilterAdapter.ViewHolder> {
    private static final String FILE_NAME = "filters.txt";

    private final List<String> filters;
    private final NavController navController;

    TextView filterName;
    ImageView edit, delete;

    String fileData;

    Filter filter;

    public SavedFilterAdapter(List<String> filters, NavController navController, String fileData) {
        this.filters = filters;
        this.navController = navController;
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

        filter = filterSplitter(fileFilter);

        filterName = holder.filterName;
        filterName.setText(filter.getName());

        edit = holder.edit;
        delete = holder.delete;

        edit.setOnClickListener(view -> {
            NavDirections action = FilterManagementFragmentDirections.actionFilterManagementFragmentToFilterCreatorFragment(fileFilter);
            navController.navigate(action);
        });

        delete.setOnClickListener(v -> {
            searchAndDelete(fileFilter, fileData, v);
            filters.remove(position);
            this.notifyItemRemoved(position);
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

    private Filter filterSplitter(String fileFilter) {
        boolean matchInfo = false, matchStats = false, setStats = false, setHistory = false, quotes = false;

        String[] splittedFilter = fileFilter.split(":");

        String[] filterTypes = splittedFilter[1].split("-");

        for (String filterType : filterTypes) {
            switch (filterType) {
                case "matchInfo":
                    matchInfo = true;
                case "matchStats":
                    matchStats = true;
                case "setStats":
                    setStats = true;
                case "setHistory":
                    setHistory = true;
                case "quotes":
                    quotes = true;
                default:
            }
        }

        return new Filter(splittedFilter[0], matchInfo, matchStats, setStats, setHistory, quotes);
    }

    private void searchAndDelete(String filterToDelete, String fileData, View v) {
        List<String> newFileData = new ArrayList<>();
        String newFileDataString = "";

        String[] fileRows = fileData.split("\n");

        for (String fileRow : fileRows) {
            if (!fileRow.equals(filterToDelete)) {
                newFileData.add(fileRow);
            }
        }

        for (String fileEntry : newFileData) {
            newFileDataString += fileEntry + "\n";
        }

        overwriteFile(newFileDataString, v);
    }

    private void overwriteFile(String newFileData, View v) {
        FileOutputStream fos = null;
        try {
            fos = v.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(newFileData.getBytes());

            Toast.makeText(v.getContext(), R.string.deleteFilter, Toast.LENGTH_LONG).show();
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