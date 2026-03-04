package me.dess1rous.skyblock.worlds.skyblock.spawn;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class EventsNPC implements Listener {

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event) {
        if (event.getClicker().getWorld().getName().equalsIgnoreCase("skyblock")) {
            if (event.getNPC().getName().equalsIgnoreCase("Пвп-арена")) {
                event.getClicker().teleport(new Location(Bukkit.getWorld("end"), 0, 60, 0));
            }

            if (event.getNPC().getName().equalsIgnoreCase("Хранитель")) {
                Inventory trader = Bukkit.createInventory(null, 54, "Хранитель");
                trader.setItem(20, new ItemStack(Material.SPONGE));
                event.getClicker().openInventory(trader);
            }

            if (event.getNPC().getName().equalsIgnoreCase("Плащи")) {
                Inventory trader = Bukkit.createInventory(null, 54, "Плащи");
                event.getClicker().openInventory(trader);
            }
        }
    }

    @EventHandler
    public void clickInInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getWhoClicked().getWorld().getName().equalsIgnoreCase("skyblock")) return;

        if (event.getView().getTitle().equalsIgnoreCase("Хранитель")) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() != Material.SPONGE) return;

            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            if (player.getTotalExperience() >= 1000) {
                removeExp(player, 1000);

                ItemStack sponge = new ItemStack(Material.SPONGE, 1);
                ItemMeta metaSponge = sponge.getItemMeta();
                metaSponge.setDisplayName(ChatColor.YELLOW + "Губка");
                metaSponge.setLore(Arrays.asList(
                        ChatColor.AQUA + "1500 Уровня Острова"
                ));
                sponge.setItemMeta(metaSponge);

                player.getInventory().addItem(sponge);
            } else {
                player.sendMessage(ChatColor.RED + "Недостаточно опыта");
            }
        }
    }

    private void removeExp(Player player, int amount) {
        int totalExp = player.getTotalExperience();
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);

        int newExp = totalExp - amount;
        if (newExp < 0) { newExp = 0; }

        player.giveExp(newExp);
    }
}