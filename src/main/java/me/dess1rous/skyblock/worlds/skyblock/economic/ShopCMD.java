package me.dess1rous.skyblock.worlds.skyblock.economic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShopCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!(player.getWorld().getName().equals("skyblock"))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cНеизвестная команда"));
            return true;
        }

        Inventory shopInv = Bukkit.createInventory(null, 54, "Магазин");

        ItemStack cobbleStone = new ItemStack(Material.COBBLESTONE);
        ItemMeta cobbleStoneMeta = cobbleStone.getItemMeta();
        cobbleStoneMeta.setDisplayName("Булыжник");
        cobbleStoneMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&a&lПродажа ЛКМ - 10.0 монет"),
                ChatColor.translateAlternateColorCodes('&', "&c&lПокупка ПКМ - 15.0 монет")));
        cobbleStone.setItemMeta(cobbleStoneMeta);
        shopInv.setItem(13, cobbleStone);

        player.openInventory(shopInv);
        return false;
    }
}
