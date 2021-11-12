package com.example.sportintelligencetesimolettadavide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Nasconde l'action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
    }
}