package me.dess1rous.skyblock;

import me.dess1rous.skyblock.database.MongoManager;
import me.dess1rous.skyblock.worlds.skyblock.economic.MoneyCollection;
import me.dess1rous.skyblock.worlds.skyblock.economic.ShopCMD;
import me.dess1rous.skyblock.worlds.skyblock.economic.ShopEvents;
import me.dess1rous.skyblock.worlds.skyblock.island.IndexCollection;
import me.dess1rous.skyblock.worlds.skyblock.island.IslandCMD;
import me.dess1rous.skyblock.worlds.skyblock.island.IslandsCollection;
import me.dess1rous.skyblock.worlds.CreateWorlds;
import me.dess1rous.skyblock.worlds.lobby.LobbyEvents;
import me.dess1rous.skyblock.worlds.skyblock.island.top.TopEvents;
import me.dess1rous.skyblock.worlds.skyblock.scoreboard.ScoreboardManager;
import me.dess1rous.skyblock.worlds.skyblock.spawn.EventsNPC;
import me.dess1rous.skyblock.worlds.skyblock.spawn.SpawnCMD;
import me.dess1rous.skyblock.worlds.end.EndEvents;
import me.dess1rous.skyblock.worlds.skyblock.spawn.SpawnNPC;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        CreateWorlds.createLobbyWorld();
        CreateWorlds.createSkyBlockWorld();
        CreateWorlds.createNetherWorld();
        CreateWorlds.createEndWorld();
        SpawnNPC.deleteNPC();
        SpawnNPC.spawnNPC();

        instance = this;
        MongoManager.connect("mongodb://localhost:27017", "myserver");

        MoneyCollection.init(MongoManager.getDatabase());
        IslandsCollection islandsCollection = new IslandsCollection(MongoManager.getDatabase());
        IndexCollection indexCollection = new IndexCollection(MongoManager.getDatabase());
        getServer().getPluginCommand("is").setExecutor(new IslandCMD(islandsCollection, indexCollection));
        getServer().getPluginCommand("spawn").setExecutor(new SpawnCMD());
        getServer().getPluginCommand("shop").setExecutor(new ShopCMD());
        getServer().getPluginManager().registerEvents(new LobbyEvents(), this);
        getServer().getPluginManager().registerEvents(new EventsNPC(), this);
        getServer().getPluginManager().registerEvents(new TopEvents(), this);
        getServer().getPluginManager().registerEvents(new EndEvents(), this);
        getServer().getPluginManager().registerEvents(new ShopEvents(), this);

        ScoreboardManager.updateScoreboard(islandsCollection);
        MoneyCollection.updateMoney();
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        MongoManager.close();
        SpawnNPC.deleteNPC();
    }
}
