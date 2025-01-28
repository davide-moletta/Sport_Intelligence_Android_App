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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteMatchAdapter extends RecyclerView.Adapter<FavouriteMatchAdapter.ViewHolder> {
    private static final String FILE_NAME = "favouriteMatches.txt";

    List<String> favouriteMatches;
    NavController navController;
    String fileData;

    Neo4J neo4j;
    FileOperations fileOperations;
    FireBase fireBase;

    SharedPreferences sharedPreferences;

    TextView tournamentName, firstPlayer, result, secondPlayer;
    ImageView fullStar;
    ConstraintLayout constraintLayout;

    public FavouriteMatchAdapter(List<String> favouriteMatches, NavController navController, Neo4J neo4j, String fileData,
                                 FireBase fireBase, SharedPreferences sharedPreferences) {
        this.favouriteMatches = favouriteMatches;
        this.navController = navController;
        this.neo4j = neo4j;
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

        View view = inflater.inflate(R.layout.item_favourite_match, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String favouriteMatch = favouriteMatches.get(position);
        int favouriteMatchId = Integer.parseInt(favouriteMatch);

        fileOperations = new FileOperations(FILE_NAME, holder.itemView);

        tournamentName = holder.tournamentName;
        firstPlayer = holder.firstPlayer;
        result = holder.result;
        secondPlayer = holder.secondPlayer;
        fullStar = holder.fullStar;
        constraintLayout = holder.constraintLayout;

        //Crea un oggetto Match in cui inserisce i dati relativi al match preferito trovato prendendoli dal database
        Match match = neo4j.fetchFavouriteDataFromId(favouriteMatchId);

        String tournamentNameString = match.getLocation() + ", " + match.getField() + " - " + match.getRound();

        tournamentName.setText(tournamentNameString);
        firstPlayer.setText(match.getFirstPlayer());
        result.setText(match.getResult());
        secondPlayer.setText(match.getSecondPlayer());

        //Imposta un OnClickListener sul match preferito per permettere la navigazione verso la pagina per la visualizzazione completa dei dati
        constraintLayout.setOnClickListener(v -> {
            NavDirections action = FavouriteMatchesFragmentDirections.actionFavouriteMatchesFragmentToSearchResultFragment(Integer.parseInt(favouriteMatches.get(holder.getAbsoluteAdapterPosition())));
            //Pulisce la lista dei match preferiti
            favouriteMatches.clear();
            navController.navigate(action);
        });

        //Imposta un OnClickListener sulla stella per permettere l'eliminazione del match preferito
        fullStar.setOnClickListener(v -> {
            //Cerca ed elimina il match selezionato
            String newFileData = fileOperations.searchAndDelete(favouriteMatch, fileData);
            Toast.makeText(v.getContext(), R.string.deleteMatch, Toast.LENGTH_SHORT).show();
            //Aggiorna i dati della recyclerView per una corretta visualizzazione
            setFileData(newFileData);
            favouriteMatches.remove(holder.getAbsoluteAdapterPosition());
            this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            if (!sharedPreferences.getString(TELEGRAM_CHAT_ID, "").equals("no ID")) {
                fireBase.updateUser();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteMatches.size();
    }

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