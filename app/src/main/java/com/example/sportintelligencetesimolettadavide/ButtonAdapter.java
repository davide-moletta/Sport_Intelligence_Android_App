package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {

    private final List<String> values;
    private final String[] searchInfo;
    private final NavController navController;

    public ButtonAdapter(List<String> values, String[] searchInfo, NavController navController) {
        this.values = values;
        this.searchInfo = searchInfo;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_button, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String value = values.get(position);

        //Adapter che andrà inserito nella recyclerView del frgamnet SearchFragment
        TextView textView = holder.textLabel;
        textView.setText(value);

        //Imposta un OnClickListener sull'elemento della recyclerView che permette di aggiornare il vettore searchInfo
        //e di navigare nella ricerca per poi arrivare ai risultati cercati
        textView.setOnClickListener(v -> {
            //Snackbar.make(v, R.string.snackBarText, Snackbar.LENGTH_SHORT).show();
            if (searchInfo[1].equals("noData")) {
                searchInfo[1] = textView.getText().toString();
                NavDirections action = SearchFragmentDirections.actionTournamentSearchFragmentSelf2(searchInfo);
                navController.navigate(action);
            } else {
                searchInfo[2] = textView.getText().toString();
                NavDirections action = SearchFragmentDirections.actionTournamentSearchFragmentToMatchSelectorFragment(searchInfo);
                navController.navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            textLabel = itemView.findViewById(R.id.textLabel);
        }
    }
}
