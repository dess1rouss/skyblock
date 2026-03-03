package me.dess1rous.skyblock;

import me.dess1rous.skyblock.database.MongoManager;
import me.dess1rous.skyblock.island.IndexCollection;
import me.dess1rous.skyblock.island.IslandCreateCMD;
import me.dess1rous.skyblock.island.IslandsCollection;
import me.dess1rous.skyblock.worlds.CreateWorlds;
import me.dess1rous.skyblock.worlds.lobby.LobbyEvents;
import me.dess1rous.skyblock.worlds.skyblock.spawn.SpawnCMD;
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

        instance = this;
        getServer().getPluginManager().registerEvents(new LobbyEvents(), this);
        getServer().getPluginCommand("spawn").setExecutor(new SpawnCMD());

        MongoManager.connect("mongodb://localhost:27017", "myserver");

        IslandsCollection islandsCollection = new IslandsCollection(MongoManager.getDatabase());
        IndexCollection indexCollection = new IndexCollection(MongoManager.getDatabase());
        getServer().getPluginCommand("is").setExecutor(new IslandCreateCMD(islandsCollection, indexCollection));
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        MongoManager.close();
    }
}
