package com.example.sportintelligencetesimolettadavide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final Neo4J neo4J = new Neo4J();
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static String TELEGRAM_CHAT_ID = "no ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Nasconde l'action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (!sharedPreferences.contains(TELEGRAM_CHAT_ID)){
            editor.putString(TELEGRAM_CHAT_ID, "no ID").commit();
        }
    }
}