import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

//Oggetto per la comunicazione con FireBase
public class FireBase {

    private Firestore db;

    public FireBase() {
        //Avvia la comunicazione con il database
        try {
            FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            this.db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //In base alla label passata ritorna il messaggio dei fitlri o dei match preferiti
    public String getData(String chatId, String label) {
        try {
            DocumentReference documentReference = db.collection("Users").document(chatId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                if (label.equals("Filters")) {
                    return filterParser(Objects.requireNonNull(documentSnapshot.getData()).get(label).toString());
                } else {
                    return favouriteMatchesParser(Objects.requireNonNull(documentSnapshot.getData()).get(label).toString());
                }
            } else {
                return "no such document";
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "error";
        }
    }

    //Metodo per raccogliere i dati relativi ai match preferiti da neo4j e per impostare il messaggio da inviare all'utente
    private String favouriteMatchesParser(String matchesToParse) {
        Neo4J neo4J = new Neo4J();
        StringBuilder matchesMessage = new StringBuilder("ECCO I TUOI MATCH PREFERITI\n\n");

        String[] matchIDs = matchesToParse.split("\n");

        for (String ID : matchIDs) {
            matchesMessage.append("---------\n");

            Match match = neo4J.fetchFavouriteMatches(Integer.parseInt(ID));

            matchesMessage.append(match.getLocation()).append("\n");
            matchesMessage.append(match.getFirstPlayer()).append("\t\t\t\t\t\t").append(match.getResult()).append("\t\t\t\t\t\t").append(match.getSecondPlayer());
            matchesMessage.append("\n---------\n\n");
        }

        neo4J.close();

        if (matchesMessage.toString().equals("ECCO I TUOI MATCH PREFERITI\n\n")) {
            return "NON HAI ANCORA SALVATO PARTITE, APRI L'APPLICAZIONE PER SALVARNE";
        } else {
            return matchesMessage.toString();
        }
    }

    //Metodo per impostare il messaggio contenente i filtri selezionati dall'utente
    private String filterParser(String filtersToParse) {
        StringBuilder filterMessage = new StringBuilder("ECCO I FILTRI CHE HAI CREATO DALL'APP\n\n");
        String[] filters = filtersToParse.split("\n");

        for (String filter : filters) {
            filterMessage.append("---------\n");

            String fName = "Nome filtro: " + filter.split(":")[0];
            String fStats = "Informazioni visualizzate dal filtro: \n" + filterBuilder(filter.split(":")[1]);

            filterMessage.append(fName).append("\n").append(fStats).append("---------\n\n");
        }

        if (filterMessage.toString().equals("ECCO I FILTRI CHE HAI CREATO DALL'APP\n\n")) {
            return "NON HAI ANCORA CREATO FILTRI, APRI L'APPLICAZIONE PER CREARNE UNO";
        } else {
            return filterMessage.toString();
        }
    }

    //In base ai filtri passati estrae una stringa che riporta i valori da mostrare all'utente
    private String filterBuilder(String filterStats) {
        String[] stats = filterStats.split("-");
        StringBuilder buildedFilter = new StringBuilder();

        for (String stat : stats) {
            if (stat.equals("matchInfo")) {
                buildedFilter.append("INFORMAZIONI PARTITA\n");
            }
            if (stat.equals("matchStats")) {
                buildedFilter.append("STATISTICHE PARTITA\n");
            }
            if (stat.equals("setStats")) {
                buildedFilter.append("STATISTICHE SET\n");
            }
            if (stat.equals("setHistory")) {
                buildedFilter.append("STORICO SET\n");
            }
            if (stat.equals("quotes")) {
                buildedFilter.append("QUOTE\n");
            }
        }

        return buildedFilter.toString();
    }
}
