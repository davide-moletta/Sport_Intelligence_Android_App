package com.example.sportintelligencetesimolettadavide;

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


    public Neo4J(String dbURL, String username, String password) {
        this.driver = GraphDatabase.driver(dbURL, AuthTokens.basic(username, password));
    }

    public void close(){
        driver.close();
    }

    public List<String> fetchChampionships(){
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n:Championship) RETURN n");
        List<String> championships = new ArrayList<>();

        while (result.hasNext()){
            Record record = result.next();

            List<Value> values = record.values();

            for(Value value: values){
                championships.add(value.get("name").asString());
            }
        }
        transaction.commit();

        return championships;
    }
}