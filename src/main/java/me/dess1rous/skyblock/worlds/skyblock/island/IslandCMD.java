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
    public static HashMap<UUID, Invite> invites = new HashMap<>();

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
        UUID uuid = player.getUniqueId();

        Island island = islandsCollection.getIsland(uuid);

        if (strings.length == 0) {
            if (!islandsCollection.hasIsland(uuid)) {
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
                if (island != null) {
                    teleportIsland(player);
                    return true;
                }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cСоздайте остров"));

                    return true;

            case "delete":

                if (island == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cНету острова чтоб удалить"));
                    return true;
                }

                if (!island.getOwner().equals(uuid)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы не создатель острова"));
                    return true;
                }

                if (strings.length == 1) {
                    deleteConfirm.add(uuid);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Для подтверждения &l/is delete confirm"));

                    Bukkit.getScheduler().runTaskLater(
                            Main.getInstance(),
                            () -> deleteConfirm.remove(uuid),
                            20 * 10
                    );
                    return true;
                }

                if (strings[1].equalsIgnoreCase("confirm")) {
                    if (!deleteConfirm.contains(uuid)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cСначала /is delete"));
                        return true;
                    }

                    deleteConfirm.remove(uuid);

                    PasteIslands paste = new PasteIslands();
                    paste.deleteIsland(
                            island.getLocation(),
                            island.getSize()
                    );
                    islandsCollection.deleteIsland(uuid);
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

                island.setName(strings[1]);
                islandsCollection.save(island);
                return true;

            case "top":

                TopManager.createTop(islandsCollection, player);
                return true;

            case "invite":
                if (strings.length != 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l/is invite NICK_OF_PLAYER"));
                    return true;
                }

                if (island == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (!island.getOwner().equals(uuid)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы не создатель острова"));
                    return true;
                }

                Player recipient = Bukkit.getPlayer(strings[1]);

                if (recipient == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгрок не найден"));
                    return true;
                }

                if (recipient == player) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы уже есть на острове"));
                    return true;
                }

                UUID uuidRecipient = recipient.getUniqueId();
                Island isRecipient = islandsCollection.getIsland(uuidRecipient);

                if (isRecipient != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгрок уже состоит в другом острове"));
                    return true;
                }

                if (invites.containsKey(uuidRecipient)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгроку уже отправлено приглашение"));
                    return true;
                }

                invites.put(uuidRecipient, new Invite(uuid));

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lВы отправили приглашение"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l" + player.getName() + " пригласил вас на остров"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l/is accept для принятия"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВнимание! &eПри соглашении вы потеряете свой остров"));

                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    if (invites.containsKey(uuidRecipient)) {
                        invites.remove(uuidRecipient);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lПриглашение истекло"));
                        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lПриглашение истекло"));
                    }
                }, 20 * 30);
                return true;

            case "accept":
                if (!invites.containsKey(uuid)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет приглашений"));
                    return true;
                }
                Invite invite = invites.get(uuid);
                invites.remove(uuid);
                Player owner = Bukkit.getPlayer(invite.getOwner());

                if (owner == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lСоздатель острова не в сети"));
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lВы приняли приглашение"));

                Island ownerIsland = islandsCollection.getIsland(owner.getUniqueId());
                ownerIsland.getMembers().add(uuid);
                islandsCollection.save(ownerIsland);

                return true;

            case "kick":
                if (strings.length != 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l/is kick NICK_OF_PLAYER"));
                    return true;
                }

                if (island == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (!island.getOwner().equals(uuid)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы не создатель острова"));
                    return true;
                }

                UUID expelUUID = null;

                for (UUID member : island.getMembers()) {
                    if (Bukkit.getOfflinePlayer(member).getName() != null
                    && Bukkit.getOfflinePlayer(member).getName().equalsIgnoreCase(strings[1])) {
                        expelUUID = member;
                        break;
                    }
                }

                if (expelUUID == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгрок не на вашем острове"));
                    return true;
                }

                island.getMembers().remove(expelUUID);
                islandsCollection.save(island);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lИгрок выгнан с острова"));

                return true;

            case "leave":
                if (strings.length != 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lНеизвестная команда"));
                    return true;
                }

                if (island == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (island.getOwner().equals(uuid)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы создатель острова"));
                    return true;
                }

                island.getMembers().remove(uuid);
                islandsCollection.save(island);
                player.teleport(new Location(
                        Bukkit.getWorld("skyblock"),
                        0.5,
                        60,
                        0.5));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lУспешно"));

                return true;

            case "list":

                if (strings.length != 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lНеизвестная команда"));
                    return true;
                }

                if (island == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lУчастники острова " + island.getName() + "&e:"));

                int i = 1;

                for (UUID member : island.getMembers()) {
                    String name = Bukkit.getOfflinePlayer(member).getName();

                    if (name == null) {
                        name = "Unknown";
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l" + i + ". &3" + name));
                    i++;
                }

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