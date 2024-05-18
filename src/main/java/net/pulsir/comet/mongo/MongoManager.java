package net.pulsir.comet.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.pulsir.comet.Comet;
import org.bson.Document;

import java.util.Objects;

@Getter
public class MongoManager {

    private final MongoCollection<Document> punishments;

    public MongoManager(){

        MongoClient mongoClient = MongoClients.create(new ConnectionString(Objects.requireNonNull(Comet.getInstance()
                .getConfiguration().getConfiguration().getString("mongo-uri"))));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("comet");
        punishments = mongoDatabase.getCollection("punishments");
    }
}
