package me.dess1rous.skyblock.island;

import me.dess1rous.skyblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class IslandCMD implements CommandExecutor {
    private final IslandsCollection islandsCollection;
    private final IndexCollection indexCollection;
    private final Set<UUID> deleteConfirm = new HashSet<>();

    public IslandCMD(IslandsCollection islandsCollection, IndexCollection indexCollection) {
        this.islandsCollection = islandsCollection;
        this.indexCollection = indexCollection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (strings.length == 0) {
            if (!islandsCollection.hasIsland(player.getUniqueId())) {
                createIsland(player);
            } else {
                teleportIsland(player);
            }
            return true;
        }

        switch (strings[0].toLowerCase()) {

            case "create":
                if (islandsCollection.getIsland(player.getUniqueId()) != null) {
                    player.sendMessage(ChatColor.RED + "Уже есть остров");
                    return true;
                }
                createIsland(player);
                return true;

            case "home":
                if (islandsCollection.getIsland(player.getUniqueId()) != null) {
                    teleportIsland(player);
                    return true;
                } else {
                    player.sendMessage("Создайте остров");
                    return true;
                }

            case "delete":
                if (islandsCollection.getIsland(player.getUniqueId()) == null) {
                    player.sendMessage("Нету острова чтоб удалить");
                    return true;
                }

                if (strings.length == 1) {
                    deleteConfirm.add(player.getUniqueId());
                    player.sendMessage(ChatColor.DARK_RED + "Для подтверждения " + ChatColor.ITALIC + "/is delete confirm");

                    Bukkit.getScheduler().runTaskLater(
                            Main.getInstance(),
                            () -> deleteConfirm.remove(player.getUniqueId()),
                            20 * 10
                    );
                    return true;
                }

                if (strings[1].equalsIgnoreCase("confirm")) {
                    if (!deleteConfirm.contains(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "Сначала /is delete");
                        return true;
                    }

                    deleteConfirm.remove(player.getUniqueId());

                    PasteIslands paste = new PasteIslands();
                    paste.deleteIsland(
                            islandsCollection.getIsland(player.getUniqueId()).getLocation(),
                            islandsCollection.getIsland(player.getUniqueId()).getSize()
                    );
                    islandsCollection.deleteIsland(player.getUniqueId());
                    player.teleport(new Location(
                            Bukkit.getWorld("skyblock"),
                            0.5,
                            60,
                            0.5
                    ));
                    return true;

                }

            case "name":
                if (strings.length == 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l/is name NAME_OF_ISLAND"));
                    return true;
                }

                if (strings[1].length() > 10
                || strings[1].length() < 4) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lНедопустимое количество символов"));
                    return true;
                }

                Island island = islandsCollection.getIsland(player.getUniqueId());

                island.setName(strings[1]);
                islandsCollection.save(island);
                return true;

            case "top":
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
                            "&a&lОстров: &c" + islandTop.getName() + " &7(#" + (i+1) + ")"));

                    List<String> lore = new ArrayList<>();

                    lore.add(ChatColor.translateAlternateColorCodes('&', "§7&l▪ Уровень острова " + islandTop.getLevel()));
                    lore.add("");
                    for (UUID member : islandTop.getMembers()) {
                        String name = Bukkit.getOfflinePlayer(member).getName();
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&b" + name));
                    }
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);

                    invTop.setItem(slots[i], item);
                }
                player.openInventory(invTop);

                return true;
        }
        return false;
    }


    private void createIsland(Player player) {
        int index =  indexCollection.incrementIndex();

        Location location = IslandsLocation.getLocationIsland(
                Bukkit.getWorld("skyblock"),
                index
        );

        PasteIslands paste = new PasteIslands();
        paste.pasteIsland(location);

        Island island = new Island(
                UUID.randomUUID(),
                location,
                ChatColor.translateAlternateColorCodes('&', "&c" + player.getName()),
                player.getUniqueId(),
                new ArrayList<>(),
                150,
                0,
                index
        );

        islandsCollection.save(island);
        player.teleport(location.clone().add(0, 1, 0));
        player.sendTitle(ChatColor.GREEN + "Остров " + player.getName(), "Успешно создан", 2, 2, 2);
    }

    private void teleportIsland(Player player) {
        Island island = islandsCollection.getIsland(player.getUniqueId());
        if (island == null) return;

        player.teleport(island.getLocation().clone().add(0, 1, 0));
    }
}