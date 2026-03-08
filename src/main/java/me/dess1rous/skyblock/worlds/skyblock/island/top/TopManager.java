package me.dess1rous.skyblock.worlds.skyblock.island.top;

import me.dess1rous.skyblock.worlds.skyblock.island.Island;
import me.dess1rous.skyblock.worlds.skyblock.island.IslandsCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TopManager {
    public static void createTop(IslandsCollection islandsCollection, Player player) {

        Material[] materials = {
                Material.DIAMOND_AXE,
                Material.GOLD_AXE,
                Material.IRON_AXE,
                Material.DIAMOND_BLOCK,
                Material.GOLD_BLOCK,
                Material.IRON_BLOCK,
                Material.DIAMOND_ORE,
                Material.GOLD_ORE,
                Material.IRON_ORE,
                Material.REDSTONE_ORE
        };
        int[] slots = {
                4,
                12,14,
                19,20,21,22,23,24,25
        };

        Inventory invTop = Bukkit.createInventory(null, 27, "Топ 10 островов");
        List<Island> top = islandsCollection.getTopIslands();

        for (int i = 0; i < top.size(); i++) {
            Island islandTop = top.get(i);
            ItemStack item = new ItemStack(materials[i]);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    "&a&lОстров: &c&l" + islandTop.getName() + " &7(#" + (i+1) + ")"));

            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.translateAlternateColorCodes('&', "§7&l▪ Уровень острова " + islandTop.getLevel()));
            for (UUID member : islandTop.getMembers()) {
                String name = Bukkit.getOfflinePlayer(member).getName();
                lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l" + name));
            }
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);

            invTop.setItem(slots[i], item);
        }
        player.openInventory(invTop);
    }
}
