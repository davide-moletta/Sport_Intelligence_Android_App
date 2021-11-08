package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SavedFilterAdapter extends RecyclerView.Adapter<SavedFilterAdapter.ViewHolder> {

    private final List<String> filters;
    private final NavController navController;

    TextView filterName;
    ImageView edit, delete;

    Filter filter;

    public SavedFilterAdapter(List<String> filters, NavController navController) {
        this.filters = filters;
        this.navController = navController;
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

        });

        delete.setOnClickListener(view -> {

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