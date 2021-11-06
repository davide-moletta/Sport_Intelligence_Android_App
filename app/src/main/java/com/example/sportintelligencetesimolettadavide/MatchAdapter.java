package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private final List<Match> matches;
    private final NavController navController;

    public MatchAdapter(List<Match> matches, NavController navController) {
        this.matches = matches;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_match, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match match = matches.get(position);

        String edition = match.getEdition();
        String firstPlayer = match.getFirstPlayer();
        String result = match.getResult();
        String secondPlayer = match.getSecondPlayer();
        String duration = match.getDuration();

        System.out.println("\n\neccoli: " + edition + firstPlayer + result + secondPlayer + duration + "\n\n");

        TextView tournamentNameView = holder.tournamentName;
        TextView firstPlayerView = holder.firstPlayer;
        TextView resultView = holder.result;
        TextView secondPlayerView = holder.secondPlayer;
        TextView durationView = holder.duration;

        tournamentNameView.setText(edition);
        firstPlayerView.setText(firstPlayer);
        resultView.setText(result);
        secondPlayerView.setText(secondPlayer);
        durationView.setText(duration);

        //on click listener
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tournamentName, firstPlayer, result, secondPlayer, duration;

        public ViewHolder(View itemView) {
            super(itemView);

            tournamentName = itemView.findViewById(R.id.tournamentName);
            firstPlayer = itemView.findViewById(R.id.firstPlayer);
            result = itemView.findViewById(R.id.result);
            secondPlayer = itemView.findViewById(R.id.secondPlayer);
            duration = itemView.findViewById(R.id.duration);
        }
    }
}
