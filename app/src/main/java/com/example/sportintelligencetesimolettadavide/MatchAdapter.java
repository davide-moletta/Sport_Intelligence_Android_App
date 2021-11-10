package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private final List<Match> matches;
    private final NavController navController;

    TextView tournamentNameView, firstPlayerView, resultView, secondPlayerView, durationView;
    ConstraintLayout constraintLayout;
    Match match;

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
        match = matches.get(position);

        tournamentNameView = holder.tournamentName;
        firstPlayerView = holder.firstPlayer;
        resultView = holder.result;
        secondPlayerView = holder.secondPlayer;
        durationView = holder.duration;

        tournamentNameView.setText(match.getLocation());
        firstPlayerView.setText(match.getFirstPlayer());
        resultView.setText(match.getResult());
        secondPlayerView.setText(match.getSecondPlayer());
        durationView.setText(match.getDuration());

        constraintLayout = holder.constraintLayout;

        constraintLayout.setOnClickListener(v -> {
            NavDirections action = MatchSelectorFragmentDirections.actionMatchSelectorFragmentToSearchResultFragment(matches.get(holder.getAbsoluteAdapterPosition()).getId());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tournamentName, firstPlayer, result, secondPlayer, duration;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tournamentName = itemView.findViewById(R.id.tournamentName);
            firstPlayer = itemView.findViewById(R.id.firstPlayer);
            result = itemView.findViewById(R.id.result);
            secondPlayer = itemView.findViewById(R.id.secondPlayer);
            duration = itemView.findViewById(R.id.duration);
            constraintLayout = itemView.findViewById(R.id.match);
        }
    }
}
