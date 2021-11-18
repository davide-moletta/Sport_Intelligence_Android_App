package com.example.sportintelligencetesimolettadavide;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sportintelligencetesimolettadavide.MainActivity.TELEGRAM_CHAT_ID;

import android.content.SharedPreferences;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FireBase {
    private static final String FILE_FILTERS_NAME = "filters.txt";
    private static final String FILE_FAVOURITE_NAME = "favouriteMatches.txt";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    SharedPreferences sharedPreferences;

    FileOperations fileFilter, fileFavouriteMatches;

    public FireBase(View view, Fragment fragment) {
        fileFilter = new FileOperations(FILE_FILTERS_NAME, view);
        fileFavouriteMatches = new FileOperations(FILE_FAVOURITE_NAME, view);
        sharedPreferences = fragment.requireActivity().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
    }

    public void updateUser() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("Filters", fileFilter.load());
        user.put("FavouriteMatches", fileFavouriteMatches.load());

        documentReference = db.collection("Users").document(sharedPreferences.getString(TELEGRAM_CHAT_ID, ""));
        documentReference.set(user);
    }
}
