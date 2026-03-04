package me.dess1rous.skyblock.island;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandsCollection {
    private final MongoCollection<Document> islands;

    public IslandsCollection(MongoDatabase database) {
        this.islands = database.getCollection("islands");
    }

    public void save(Island island) {
        Location loc = island.getLocation();

        List<String> membersString = new ArrayList<>();

        for (UUID uuid : island.getMembers()) {
            membersString.add(uuid.toString());
        }

        Document docLocation = new Document()
                .append("world", loc.getWorld().getName())
                .append("x", loc.getBlockX())
                .append("y", loc.getBlockY())
                .append("z", loc.getBlockZ());

        Document doc = new Document()
                .append("_id", island.getIslandID().toString())
                .append("location", docLocation)
                .append("owner", island.getOwner().toString())
                .append("players", membersString)
                .append("size", island.getSize())
                .append("level", island.getLevel())
                .append("index", island.getIndex());
        islands.insertOne(doc);
    }

    public boolean hasIsland(UUID uuid) {
        return islands.find(Filters.eq("owner", uuid.toString())).first() != null;
    }

    public void deleteIsland(UUID uuid) {
        islands.deleteOne(Filters.eq("owner", uuid.toString()));
    }

    public Island getIsland(UUID uuid) {
        Document doc = islands.find(Filters.eq("owner", uuid.toString())).first();
        if (doc == null) return null;

        Document locationDoc = (Document) doc.get("location");

        Location location = new Location(
                Bukkit.getWorld(locationDoc.getString("world")),
                locationDoc.getInteger("x"),
                locationDoc.getInteger("y"),
                locationDoc.getInteger("z")
                );

        List<String> membersString = (List<String>) doc.get("players");
        List<UUID> members = new ArrayList<>();

        if (membersString != null) {
            for (String s : membersString) {
                members.add(UUID.fromString(s));
            }
        }

        return new Island(
                UUID.fromString(doc.getString("_id")),
                location,
                UUID.fromString(doc.getString("owner")),
                members,
                doc.getInteger("size"),
                doc.getInteger("level"),
                doc.getInteger("index")
        );
    }
}
