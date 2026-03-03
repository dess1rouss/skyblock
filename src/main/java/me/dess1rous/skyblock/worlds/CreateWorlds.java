package me.dess1rous.skyblock.worlds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class CreateWorlds {

    public static void createLobbyWorld() {
        if (Bukkit.getWorld("lobby") != null) return;

        WorldCreator creator = new WorldCreator("lobby");
        creator.generator(new VoidGeneration());
        creator.createWorld();
        setupWorld(Bukkit.getWorld("lobby"));
    }

    public static void createSkyBlockWorld() {
        if (Bukkit.getWorld("skyblock") != null) return;

        WorldCreator creator = new WorldCreator("skyblock");
        creator.generator(new VoidGeneration());
        creator.createWorld();
        setupWorld(Bukkit.getWorld("skyblock"));
    }

    public static void createNetherWorld() {
        if (Bukkit.getWorld("nether") != null) return;

        WorldCreator creator = new WorldCreator("nether");

        creator.environment(World.Environment.NETHER);
        creator.generator(new VoidGeneration());
        creator.createWorld();
        setupWorld(Bukkit.getWorld("nether"));
    }

    public static void createEndWorld() {
        if (Bukkit.getWorld("end") != null) return;

        WorldCreator creator = new WorldCreator("end");

        creator.environment(World.Environment.THE_END);
        creator.generator(new VoidGeneration());
        creator.createWorld();
        setupWorld(Bukkit.getWorld("end"));
    }

    private static void setupWorld(World world) {
        if (world == null) return;

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setTime(1200);
        world.setStorm(false);
    }
}