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

                Island isForDelete = islandsCollection.getIsland(player.getUniqueId());

                if (isForDelete == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cНету острова чтоб удалить"));
                    return true;
                }

                if (!isForDelete.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы не создатель острова"));
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
                            isForDelete.getLocation(),
                            isForDelete.getSize()
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

                Island isForName = islandsCollection.getIsland(player.getUniqueId());

                isForName.setName(strings[1]);
                islandsCollection.save(isForName);
                return true;

            case "top":

                TopManager.createTop(islandsCollection, player);
                return true;

            case "invite":
                if (strings.length != 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l/is invite NICK_OF_PLAYER"));
                    return true;
                }

                Island isForInvite = islandsCollection.getIsland(player.getUniqueId());

                if (isForInvite == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (!isForInvite.getOwner().equals(player.getUniqueId())) {
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

                Island isRecipient = islandsCollection.getIsland(recipient.getUniqueId());

                if (isRecipient != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгрок уже состоит в другом острове"));
                    return true;
                }

                if (invites.containsKey(recipient.getUniqueId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lИгроку уже отправлено приглашение"));
                    return true;
                }

                invites.put(recipient.getUniqueId(), new Invite(player.getUniqueId()));

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lВы отправили приглашение"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l" + player.getName() + " пригласил вас на остров"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l/is accept для принятия"));
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВнимание! &eПри соглашении вы потеряете свой остров"));

                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    if (invites.containsKey(recipient.getUniqueId())) {
                        invites.remove(recipient.getUniqueId());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lПриглашение истекло"));
                        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lПриглашение истекло"));
                    }
                }, 20 * 30);
                return true;

            case "accept":
                if (!invites.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет приглашений"));
                    return true;
                }
                Invite invite = invites.get(player.getUniqueId());
                invites.remove(player.getUniqueId());
                Player owner = Bukkit.getPlayer(invite.getOwner());

                if (owner == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lСоздатель острова не в сети"));
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lВы приняли приглашение"));

                Island isForAdd = islandsCollection.getIsland(owner.getUniqueId());
                isForAdd.getMembers().add(player.getUniqueId());
                islandsCollection.save(isForAdd);

                return true;

            case "kick":
                if (strings.length != 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l/is kick NICK_OF_PLAYER"));
                    return true;
                }

                Island isForKick = islandsCollection.getIsland(player.getUniqueId());

                if (isForKick == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (!isForKick.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы не создатель острова"));
                    return true;
                }

                UUID expelUUID = null;

                for (UUID member : isForKick.getMembers()) {
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

                isForKick.getMembers().remove(expelUUID);
                islandsCollection.save(isForKick);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lИгрок выгнан с острова"));

                return true;

            case "leave":
                if (strings.length != 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lНеизвестная команда"));
                    return true;
                }

                Island isForLeave = islandsCollection.getIsland(player.getUniqueId());

                if (isForLeave == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lУ вас нет острова"));
                    return true;
                }

                if (isForLeave.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lВы создатель острова"));
                    return true;
                }

                isForLeave.getMembers().remove(player.getUniqueId());
                islandsCollection.save(isForLeave);
                player.teleport(new Location(
                        Bukkit.getWorld("skyblock"),
                        0.5,
                        60,
                        0.5));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lУспешно"));

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