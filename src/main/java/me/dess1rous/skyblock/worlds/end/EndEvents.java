package me.dess1rous.skyblock.worlds.end;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EndEvents implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
            event.setRespawnLocation(new Location(
                    Bukkit.getWorld("skyblock"),
                    0.5,
                    60,
                    0.5
            ));
        }
    }