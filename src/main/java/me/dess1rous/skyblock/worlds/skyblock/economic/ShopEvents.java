package me.dess1rous.skyblock.worlds.skyblock.economic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopEvents implements Listener {

    @EventHandler
    public void shopClickItem(InventoryClickEvent event) {
        if (!event.getWhoClicked().getWorld().getName().equalsIgnoreCase("skyblock")) return;
        if (!event.getInventory().getName().equalsIgnoreCase("Магазин")
                && !(event.getInventory().getHolder() == null)) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        switch (event.getCurrentItem().getType()) {

            case COBBLESTONE:

                if (event.isRightClick()) {
                    double price = 15.0;
                    double balance = MoneyCollection.getMoney(uuid);

                    if (balance >= price) {
                        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 1));
                        MoneyCollection.removeMoney(uuid, price);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lУспешно!"));
                        event.setCancelled(true);
                        return;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lНедостаточно средств"));
                    event.setCancelled(true);
                    return;
                }

                if (event.isLeftClick()) {

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() == Material.COBBLESTONE) {
                            item.setAmount(item.getAmount() - 1);

                            if (item.getAmount() <= 0) {
                                player.getInventory().remove(item);
                            }

                            MoneyCollection.addMoney(uuid, 10.0);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lУспешно!"));
                            event.setCancelled(true);
                            return;
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет этого предмета"));
                        event.setCancelled(true);
                        return;
                    }
                    event.setCancelled(true);
                    return;
                }
    }
}
}
