package me.dess1rous.skyblock.worlds.skyblock.island;

import me.dess1rous.skyblock.Main;
import me.dess1rous.skyblock.worlds.skyblock.island.top.TopManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cУже есть остров"));
                    return true;
                }
                createIsland(player);
                return true;

            case "home":
                if (islandsCollection.getIsland(player.getUniqueId()) != null) {
                    teleportIsland(player);
                    return true;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cСоздайте остров"));
                    return true;
                }

            case "delete":
                if (islandsCollection.getIsland(player.getUniqueId()) == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cНету острова чтоб удалить"));
                    return true;
                }

                if (strings.length == 1) {
                    deleteConfirm.add(player.getUniqueId());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Для подтверждения &l/is delete confirm"));

                    Bukkit.getScheduler().runTaskLater(
                            Main.getInstance(),
                            () -> deleteConfirm.remove(player.getUniqueId()),
                            20 * 10
                    );
                    return true;
                }

                if (strings[1].equalsIgnoreCase("confirm")) {
                    if (!deleteConfirm.contains(player.getUniqueId())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cСначала /is delete"));
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

                TopManager.createTop(islandsCollection, player);
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

        List<UUID> members = new ArrayList<>();
        members.add(player.getUniqueId());

        Island island = new Island(
                UUID.randomUUID(),
                location,
                ChatColor.translateAlternateColorCodes('&', "&c" + player.getName()),
                player.getUniqueId(),
                members,
                150,
                0,
                index
        );

        islandsCollection.save(island);
        player.teleport(location.clone().add(0, 1, 0));
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "Остров " + player.getName()),
                ChatColor.translateAlternateColorCodes('&', "Успешно создан"), 4, 2, 4);
    }

    private void teleportIsland(Player player) {
        Island island = islandsCollection.getIsland(player.getUniqueId());
        if (island == null) return;

        player.teleport(island.getLocation().clone().add(0, 1, 0));
    }
}