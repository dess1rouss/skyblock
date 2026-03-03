package me.dess1rous.skyblock.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoManager {

    private static MongoClient client;
    private static MongoDatabase database;

    public static void connect(String uri, String dbName) {
        client = MongoClients.create(uri);
        database = client.getDatabase(dbName);
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}
