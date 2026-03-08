package me.dess1rous.skyblock.worlds.skyblock.island.top;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TopEvents implements Listener {

    @EventHandler
    public void onInventoryTop(InventoryClickEvent event) {
        if (!event.getWhoClicked().getWorld().getName().equalsIgnoreCase("skyblock")) return;
        if (!event.getInventory().getName().equalsIgnoreCase("Топ 10 островов")) return;

        event.setCancelled(true);
    }
}
