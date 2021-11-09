package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class FavouriteMatchAdapter extends RecyclerView.Adapter<FavouriteMatchAdapter.ViewHolder> {

    List<String> favouriteMatches;
    NavController navController;

    Neo4J neo4J;

    TextView tournamentName, firstPlayer, result, secondPlayer;
    ImageView fullStar;
    ConstraintLayout constraintLayout;

    public FavouriteMatchAdapter(List<String> favouriteMatches, NavController navController) {
        this.favouriteMatches = favouriteMatches;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_favourite_match, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        tournamentName = holder.tournamentName;
        firstPlayer = holder.firstPlayer;
        result = holder.result;
        secondPlayer = holder.secondPlayer;
        fullStar = holder.fullStar;
        constraintLayout = holder.constraintLayout;

        //CODICE PER MOSTRARE LA LISTA


        constraintLayout.setOnClickListener(v -> {
            NavDirections action = FavouriteMatchesFragmentDirections.actionFavouriteMatchesFragmentToSearchResultFragment(0);
            navController.navigate(action);
        });

        fullStar.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() { return favouriteMatches.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tournamentName, firstPlayer, result, secondPlayer;
        private final ImageView fullStar;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tournamentName = itemView.findViewById(R.id.tournamentName);
            firstPlayer = itemView.findViewById(R.id.firstPlayer);
            result = itemView.findViewById(R.id.result);
            secondPlayer = itemView.findViewById(R.id.secondPlayer);
            fullStar = itemView.findViewById(R.id.fullStar);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}