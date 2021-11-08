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
    final String dbURL = "bolt://192.168.1.5:7687", username = "neo4j", password = "admin";

    public Neo4J() {
        this.driver = GraphDatabase.driver(dbURL, AuthTokens.basic(username, password));
    }

    public void close() {
        driver.close();
    }

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

    public List<String> fetchTournamentEditions(String tournament) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Championship), (m:Edition) WHERE n.name=$tournament AND (n)-[]-(m) RETURN m", parameters("tournament", tournament));
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

    public List<Match> fetchTournamentMatches(String edition) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Edition), (m:Game) WHERE n.edName=$edition AND (n)-[]-(m) RETURN ID(m)", parameters("edition", edition));

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

    public List<String> fetchAthletesEditions(String athlete) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Player), (m:Edition) WHERE n.playerName=$athlete AND (n)-[]-()-[]-(m) RETURN m", parameters("athlete", athlete));
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

    public List<Match> fetchAthletesMatches(String athlete, String edition) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (m:Game), (n:Edition) WHERE n.edName=$edition AND (n)-[]-(m) AND (m.firstPlayer=$athlete OR m.secondPlayer=$athlete) RETURN ID(m)", parameters("edition", edition, "athlete", athlete));

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



    private Match fetchDataFromId(int id) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n) WHERE ID(n)=$id RETURN n", parameters("id", id));

        Match match = null;

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                match = new Match(id, value.get("location").asString(), value.get("firstPlayer").asString(), value.get("result").asString(), value.get("secondPlayer").asString(), value.get("duration").asString());
            }
        }
        transaction.commit();

        return match;
    }
}