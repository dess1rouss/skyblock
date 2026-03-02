package me.dess1rous.skyblock.island;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;

public class IslandsCollection {
    private final MongoCollection<Document> islands;

    public IslandsCollection(MongoDatabase database) {
        this.islands = database.getCollection("islands");
    }

    public void save(Island island) {
        Document doc = new Document()
                .append("_id", island.getUuid().toString())
                .append("location", island.getLocation())
                .append("owner", island.getOwner())
                .append("players", island.getMembers())
                .append("size", island.getSize())
                .append("level", island.getLevel())
                .append("index", island.getIndex());
        islands.insertOne(doc);
    }

    public boolean hasIsland(UUID uuid) {
        return islands.find(Filters.eq("_id", uuid.toString())).first() != null;
    }
}
