package me.dess1rous.skyblock.worlds.skyblock.spawn;


import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnNPC {
    public static void spawnNPC() {
        CitizensAPI.removeNamedNPCRegistry("Пвп-арена");
        CitizensAPI.removeNamedNPCRegistry("Хранитель");
        CitizensAPI.removeNamedNPCRegistry("Плащи");

    NPCRegistry registry1 = CitizensAPI.getNPCRegistry();
    NPCRegistry registry2 = CitizensAPI.getNPCRegistry();
    NPCRegistry registry3 = CitizensAPI.getNPCRegistry();

    Location locArena = new Location(Bukkit.getWorld("skyblock"), 8.5, 60, 5.5, 135, 0);
    Location locTrader = new Location(Bukkit.getWorld("skyblock"), 9.5, 60, 0.5, 90, 0);
    Location locTags = new Location(Bukkit.getWorld("skyblock"), 8.5, 60, -4.5, 45, 0);

    NPC arena = registry1.createNPC(EntityType.PLAYER, "Пвп-арена");
    NPC trader = registry2.createNPC(EntityType.PLAYER, "Хранитель");
    NPC tags = registry3.createNPC(EntityType.PLAYER, "Плащи");
    arena.spawn(locArena);
    trader.spawn(locTrader);
    tags.spawn(locTags);
  }
}
