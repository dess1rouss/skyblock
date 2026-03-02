package me.dess1rous.skyblock.island;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class IndexCollection {

    private final MongoCollection<Document> index;

    public IndexCollection(MongoDatabase database) {
        this.index = database.getCollection("index");
    }

    public int incrementIndex() {
        Document doc = index.findOneAndUpdate(
                Filters.eq("_id", "island_index"),
                Updates.inc("value", 1),
                new FindOneAndUpdateOptions()
                        .upsert(true)
                        .returnDocument(ReturnDocument.AFTER)
        );
        return doc.getInteger("value");
    }
}
