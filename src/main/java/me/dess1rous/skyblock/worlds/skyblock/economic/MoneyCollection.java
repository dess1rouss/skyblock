package me.dess1rous.skyblock.worlds.skyblock.economic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.dess1rous.skyblock.Main;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoneyCollection {

    private static MongoCollection<Document> money;
    private static final Map<UUID, Double> cache = new HashMap<>();

    public static void init(MongoDatabase database) {
        money = database.getCollection("money");
    }

    public static void loadMoney(UUID uuid) {
        Document doc = money.find(Filters.eq("_id", uuid.toString())).first();

        if (doc == null) {
            cache.put(uuid, 0.0);
            return;
        }

        cache.put(uuid, doc.getDouble("balance"));
    }

    public static void saveMoney(UUID uuid) {
        double balance = getMoney(uuid);

        Document doc = new Document()
                .append("_id", uuid.toString())
                .append("balance", balance);

        money.replaceOne(
                Filters.eq("_id", uuid.toString()),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    public static void unloadMoney(UUID uuid) {
        saveMoney(uuid);
        cache.remove(uuid);
    }

    public static void updateMoney() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (UUID uuid : cache.keySet().toArray(new UUID[0])) {
                saveMoney(uuid);
            }
        }, 20 * 60, 20 *  60);
    }

    public static double getMoney(UUID uuid) {
        return cache.getOrDefault(uuid, 0.0);
    }

    public static void setMoney(UUID uuid, double amount) {
        cache.put(uuid, amount);
    }

    public static void addMoney(UUID uuid, double amount) {
        cache.put(uuid, getMoney(uuid) + amount);
    }

    public static void removeMoney(UUID uuid, double amount) {
        cache.put(uuid, getMoney(uuid) - amount);
    }

    public static boolean hasMoney(UUID uuid, double amount) {
        return getMoney(uuid) >= amount;
    }


}
