package me.dess1rous.skyblock.worlds.lobby;

import me.dess1rous.skyblock.worlds.VoidGeneration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            World world = Bukkit.getWorld("lobby");
            event.getPlayer().teleport(new Location(world, Math.random() * 100000, 60, Math.random() * 100000));
        }
            ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
            ItemMeta meta = spawner.getItemMeta();
            meta.setDisplayName("SkyBlock Kingdom");
            spawner.setItemMeta(meta);
            Inventory inv = event.getPlayer().getInventory();
            inv.setItem(4, spawner);
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void breakEvent(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeEvent(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("lobby")
                || event.getEntity().getWorld().getName().equalsIgnoreCase("skyblock")
                || event.getEntity().getWorld().getName().equalsIgnoreCase("nether")
                || event.getEntity().getWorld().getName().equalsIgnoreCase("end")) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR &&
                    event.getPlayer().getInventory().getItemInMainHand().getType() == Material.MOB_SPAWNER) {
                event.setCancelled(true);

                World world = Bukkit.getWorld("skyblock");

                if (world == null) return;

                event.getPlayer().teleport(new Location(world, 0.5, 60, 0.5));
                event.getPlayer().getInventory().clear();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        World world = Bukkit.getWorld("lobby");
        event.getPlayer().teleport(new Location(world, Math.random() * 100000, 60, Math.random() * 100000));
    }
}
