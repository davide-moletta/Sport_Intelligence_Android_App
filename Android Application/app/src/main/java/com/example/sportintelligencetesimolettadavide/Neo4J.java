package com.example.sportintelligencetesimolettadavide;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;

import java.util.ArrayList;
import java.util.List;

//Neo4J comunication object
public class Neo4J {

    Driver driver;
    //dbURL è l'indirizzo IP esterno del server in cui è attivo il database neo4j
    final String dbURL = "bolt://188.152.147.252:7687", username = "neo4j", password = "admin";

    public Neo4J() {
        this.driver = GraphDatabase.driver(dbURL, AuthTokens.basic(username, password));
    }

    //Trova tutti i campionati presenti sul database
    public List<String> fetchChampionships() {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Championship) RETURN n");
        List<String> championships = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                championships.add(value.get("name").asString());
            }
        }
        transaction.commit();

        return championships;
    }

    //Trova tutte le edizioni presenti sul database relative ad uno specifico campionato
    public List<String> fetchTournamentEditions(String tournament) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Championship), (m:Edition) WHERE n.name=$tournament AND (n)-[]-(m) RETURN m",
                parameters("tournament", tournament));
        List<String> editions = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                editions.add(value.get("edName").asString());
            }
        }
        transaction.commit();

        return editions;
    }

    //Trova tutti i match presenti sul database relativi ad una specifica edizione
    public List<Match> fetchTournamentMatches(String edition) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Edition), (m:Game) WHERE n.edName=$edition AND (n)-[]-(m) RETURN ID(m)",
                parameters("edition", edition));

        List<Integer> ids = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                ids.add(value.asInt());
            }
        }
        transaction.commit();

        for (int id : ids) {
            matches.add(fetchDataFromId(id));
        }

        return matches;
    }

    //Trova tutti gli atleti presenti sul database
    public List<String> fetchAthletes() {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Player) RETURN n");
        List<String> athletes = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                athletes.add(value.get("playerName").asString());
            }
        }
        transaction.commit();

        return athletes;
    }

    //Trova tutte le edizioni in cui uno specifico atleta ha giocato
    public List<String> fetchAthletesEditions(String athlete) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Player), (m:Edition) WHERE n.playerName=$athlete AND (n)-[]-()-[]-(m) RETURN m",
                parameters("athlete", athlete));
        List<String> athletesEditions = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                athletesEditions.add(value.get("edName").asString());
            }
        }
        transaction.commit();

        return athletesEditions;
    }

    //Trova tutti i match giocati da un giocatore in una specifica edizione
    public List<Match> fetchAthletesMatches(String athlete, String edition) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (m:Game), (n:Edition) WHERE n.edName=$edition AND (n)-[]-(m) AND (m.firstPlayer=$athlete " +
                "OR m.secondPlayer=$athlete) RETURN ID(m)", parameters("edition", edition, "athlete", athlete));

        List<Integer> ids = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                ids.add(value.asInt());
            }
        }
        transaction.commit();

        for (int id : ids) {
            matches.add(fetchDataFromId(id));
        }

        return matches;
    }

    //Trova alcune informazioni relative ad un match con uno specifico ID
    private Match fetchDataFromId(int id) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n) WHERE ID(n)=$id RETURN n", parameters("id", id));

        Match match = null;

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                match = new Match(id, value.get("location").asString(), value.get("firstPlayer").asString(),
                        value.get("result").asString(), value.get("secondPlayer").asString(), value.get("length").asString());
            }
        }
        transaction.commit();

        return match;
    }

    //Trova le informazioni utili alla visualizzazione del amtch preferito relative ad un match con uno specifico ID
    public Match fetchFavouriteDataFromId(int id) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n) WHERE ID(n)=$id RETURN n", parameters("id", id));

        Match match = null;

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                match = new Match(id, value.get("location").asString(), value.get("firstPlayer").asString(),
                        value.get("result").asString(), value.get("secondPlayer").asString(),
                        value.get("field").asString(), value.get("round").asString());
            }
        }
        transaction.commit();

        return match;
    }

    //Trova l'edizione di appertenenza di un mathc con uno specifico ID
    public String fetchEditionFromId(int id) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n), (m:Edition) WHERE ID(n)=$id AND (n)-[]-(m) RETURN m.edName", parameters("id", id));

        String edition = "";

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                edition = value.asString();
            }
        }
        transaction.commit();

        return edition;
    }

    //Trova tutte le informazioni relative ad un match con uno specifico ID
    public Match fetchAllDataFromId(int id) {
        Transaction transaction = driver.session().beginTransaction();
        Result DBresult = transaction.run("MATCH (n) WHERE ID(n)=$id RETURN n, keys(n)", parameters("id", id));

        Match match = null;

        while (DBresult.hasNext()) {
            Record record = DBresult.next();

            List<Value> values = record.values();

            String location = "", firstPlayer = "", result = "", secondPlayer = "", duration = "", date = "", field = "", round = "";
            List<Object> matchStats = null, quotes = null;
            List[] setsStats = new List[10];
            List[] setsHistory = new List[10];
            List[] setsFifteens = new List[10];
            List[] setsTiebreaks = new List[10];

            //Salva le chiavi dei valori trovati
            Value keys = values.get(1);
            //Salva i valori trovati
            Value node = values.get(0);

            List<Object> objects = keys.asList();

            //Per ogni chiave controlla cos'è e in base a quello che trova decide dove inserirlo
            for (Object object : objects) {
                if (object.equals("location")) {
                    location = node.get(object.toString()).asString();
                }
                if (object.equals("date")) {
                    date = node.get(object.toString()).asString();
                }
                if (object.equals("field")) {
                    field = node.get(object.toString()).asString();
                }
                if (object.equals("firstPlayer")) {
                    firstPlayer = node.get(object.toString()).asString();
                }
                if (object.equals("length")) {
                    duration = node.get(object.toString()).asString();
                }
                if (object.equals("secondPlayer")) {
                    secondPlayer = node.get(object.toString()).asString();
                }
                if (object.equals("round")) {
                    round = node.get(object.toString()).asString();
                }
                if (object.equals("result")) {
                    result = node.get(object.toString()).asString();
                }

                if (object.equals("matchStat")) {
                    matchStats = node.get(object.toString()).asList();
                }
                if (object.equals("quotes")) {
                    quotes = node.get(object.toString()).asList();
                }

                if (object.toString().contains("Fifteens")) {
                    String number = object.toString().split("Fifteens")[0].split("set")[1];

                    int setNumber = Integer.parseInt(number);
                    setsFifteens[setNumber - 1] = node.get(object.toString()).asList();
                }
                if (object.toString().contains("Games")) {
                    String number = object.toString().split("Games")[0].split("set")[1];

                    int setNumber = Integer.parseInt(number);
                    setsHistory[setNumber - 1] = node.get(object.toString()).asList();
                }
                if (object.toString().contains("Tiebreaks")) {
                    String number = object.toString().split("Tiebreaks")[0].split("set")[1];

                    int setNumber = Integer.parseInt(number);
                    setsTiebreaks[setNumber - 1] = node.get(object.toString()).asList();
                }
                if (object.toString().contains("Stat") && !object.equals("matchStat")) {
                    String number = object.toString().split("Stat")[0].split("set")[1];

                    int setNumber = Integer.parseInt(number);
                    setsStats[setNumber - 1] = node.get(object.toString()).asList();
                }

            }

            match = new Match(id, location, firstPlayer, result, secondPlayer, duration, date, field,
                    round, matchStats, setsStats, setsHistory, setsFifteens, setsTiebreaks, quotes);
        }
        transaction.commit();

        return match;
    }
}