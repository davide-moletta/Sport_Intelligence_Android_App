package com.example.sportintelligencetesimolettadavide;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sportintelligencetesimolettadavide.MainActivity.TELEGRAM_CHAT_ID;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TelegramBotFragment extends Fragment {
    EditText chatId;
    ImageView back;
    TextView saveButton;

    FireBase fireBase;

    String telegramChatID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    NavController navController;

    public TelegramBotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_telegram_bot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Ottiene il valore della chat telegram tramite le SharedPreferences
        sharedPreferences = this.requireActivity().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        telegramChatID = sharedPreferences.getString(TELEGRAM_CHAT_ID, "");

        fireBase = new FireBase(view, this);
        navController = Navigation.findNavController(view);

        chatId = view.findViewById(R.id.chatId);
        saveButton = view.findViewById(R.id.saveChatid);
        back = view.findViewById(R.id.back);

        //Se il valore della chat telegram era già stato impostato disabilita l'edit text e il bottone salva
        if (!telegramChatID.equals("no ID")) {
            chatId.setText(telegramChatID);
            chatId.setEnabled(false);
            saveButton.setEnabled(false);
        }

        //Se l'utente clicca su salva viene controllato se è stato inserito qualcosa nel campo chatID
        saveButton.setOnClickListener(v -> {
            if (chatId.getText().toString().equals("")) {
                Toast.makeText(v.getContext(), R.string.noChatID, Toast.LENGTH_SHORT).show();
            } else {
                //Se l'utente ha inserito qualcosa viene aggiornato il valore della chatID
                editor.putString(TELEGRAM_CHAT_ID, chatId.getText().toString()).commit();

                //edit text e bottone sono disabilitati
                chatId.setEnabled(false);
                saveButton.setEnabled(false);

                //L'utente viene inserito nel database FireBase
                fireBase.updateUser();
            }
        });

        back.setOnClickListener(v -> navController.navigateUp());
    }
}