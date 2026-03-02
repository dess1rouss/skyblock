package me.dess1rous.skyblock;

import me.dess1rous.skyblock.database.MongoManager;
import me.dess1rous.skyblock.island.IndexCollection;
import me.dess1rous.skyblock.island.IslandCreateCMD;
import me.dess1rous.skyblock.island.IslandsCollection;
import me.dess1rous.skyblock.worlds.VoidGeneration;
import me.dess1rous.skyblock.worlds.lobby.LobbyEvents;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        createLobbyWorld();
        createSkyBlockWorld();
        createNetherWorld();
        createEndWorld();
        instance = this;
        MongoManager.connect("mongodb://localhost:27017", "myserver");

        IslandsCollection islandsCollection = new IslandsCollection(MongoManager.getDatabase());
        IndexCollection indexCollection = new IndexCollection(MongoManager.getDatabase());

        getServer().getPluginManager().registerEvents(new LobbyEvents(), this);
        getServer().getPluginCommand("is").setExecutor(new IslandCreateCMD(islandsCollection, indexCollection));
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        MongoManager.close();
    }

    private void createLobbyWorld() {
        if (Bukkit.getWorld("lobby") != null) return;

        WorldCreator creator = new WorldCreator("lobby");
        creator.generator(new VoidGeneration());
        creator.createWorld();
    }

    private void createSkyBlockWorld() {
        if (Bukkit.getWorld("skyblock") != null) return;

        WorldCreator creator = new WorldCreator("skyblock");
        creator.generator(new VoidGeneration());
        creator.createWorld();
    }

    private void createNetherWorld() {
        if (Bukkit.getWorld("nether") != null) return;

        WorldCreator creator = new WorldCreator("nether");
        creator.generator(new VoidGeneration());
        creator.createWorld();
    }

    private void createEndWorld() {
        if (Bukkit.getWorld("end") != null) return;

        WorldCreator creator = new WorldCreator("end");
        creator.generator(new VoidGeneration());
        creator.createWorld();
    }
}
