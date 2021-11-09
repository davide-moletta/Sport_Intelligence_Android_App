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

import java.util.List;


public class SavedFilterAdapter extends RecyclerView.Adapter<SavedFilterAdapter.ViewHolder> {
    private static final String FILE_NAME = "filters.txt";

    private final List<String> filters;
    private final NavController navController;

    TextView filterName;
    ImageView edit, delete;

    String fileData;

    FileOperations fileOperations;
    Filter filter;

    public SavedFilterAdapter(List<String> filters, NavController navController, String fileData) {
        this.filters = filters;
        this.navController = navController;
        this.fileData = fileData;
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

        filter = filterSplitter(fileFilter);

        fileOperations = new FileOperations(FILE_NAME, holder.itemView);

        filterName = holder.filterName;
        filterName.setText(filter.getName());

        edit = holder.edit;
        delete = holder.delete;

        edit.setOnClickListener(view -> {
            NavDirections action = FilterManagementFragmentDirections.actionFilterManagementFragmentToFilterCreatorFragment(fileFilter);
            filters.clear();
            navController.navigate(action);

        });

        delete.setOnClickListener(v -> {
            String newFileData = fileOperations.searchAndDelete(fileFilter, fileData);
            Toast.makeText(v.getContext(), R.string.deleteFilter, Toast.LENGTH_LONG).show();
            setFileData(newFileData);
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
}