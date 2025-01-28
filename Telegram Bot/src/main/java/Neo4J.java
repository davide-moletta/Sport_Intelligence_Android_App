import org.neo4j.driver.*;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

//Oggetto per la comunicazione con il database Neo4j
public class Neo4J {
    //dbURI è l'indirizzo locale del database (Se stessa macchina) oppure l'indirizzo remoto del database
    private final static String dbURI = "bolt://192.168.1.5:7687", user = "neo4j", password = "admin";
    private final Driver driver;

    public Neo4J() {
        driver = GraphDatabase.driver(dbURI, AuthTokens.basic(user, password));
    }

    //Chiude la connessione col database
    public void close() {
        driver.close();
    }

    //Dato l'id di un nodo trova il match relativo nel database e ritorna il risultato sotto forma di un oggetto match
    public Match fetchFavouriteMatches(int ID) {
        Transaction transaction = driver.session().beginTransaction();
        Result result = transaction.run("MATCH (n) WHERE ID(n)=$ID RETURN n", parameters("ID", ID));

        Match match = null;

        while (result.hasNext()) {
            Record record = result.next();

            List<Value> values = record.values();

            for (Value value : values) {
                match = new Match(value.get("firstPlayer").asString(), value.get("secondPlayer").asString(),
                        value.get("result").asString(), value.get("location").asString());
            }
        }
        transaction.commit();

        return match;
    }
}
