package me.dess1rous.skyblock.worlds.skyblock.spawn;


import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class SpawnNPC {
    public static void deleteNPC() {

        NPCRegistry registry = CitizensAPI.getNPCRegistry();

        List<NPC> toRemove = new ArrayList<>();

        for (NPC npc : registry) {
            String name = npc.getName();
            if (name.equals("Пвп-арена")
                    || name.equals("Хранитель")
                    || name.equals("Плащи")) {
                toRemove.add(npc);
            }
        }

        for (NPC npc : toRemove) {
            npc.despawn();
            registry.deregister(npc);
        }
    }

    public static void spawnNPC() {
    NPCRegistry registry = CitizensAPI.getNPCRegistry();

    NPC arena = registry.createNPC(EntityType.PLAYER, "Пвп-арена");
        arena.spawn(new Location(Bukkit.getWorld("skyblock"), 8.5, 60, 5.5, 135, 0));
    NPC trader = registry.createNPC(EntityType.PLAYER, "Хранитель");
        trader.spawn(new Location(Bukkit.getWorld("skyblock"), 9.5, 60, 0.5, 90, 0));
    NPC tags = registry.createNPC(EntityType.PLAYER, "Плащи");
        tags.spawn(new Location(Bukkit.getWorld("skyblock"), 8.5, 60, -4.5, 45, 0));
  }
}
