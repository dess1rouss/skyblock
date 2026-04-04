package me.dess1rous.skyblock.worlds.skyblock.scoreboard;

import me.dess1rous.skyblock.Main;
import me.dess1rous.skyblock.worlds.skyblock.economic.MoneyCollection;
import me.dess1rous.skyblock.worlds.skyblock.island.Island;
import me.dess1rous.skyblock.worlds.skyblock.island.IslandCMD;
import me.dess1rous.skyblock.worlds.skyblock.island.IslandsCollection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private static final Map<UUID, Scoreboard> boards = new HashMap<>();

    public static void createScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("sb", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lKingdom &7[&f" + Bukkit.getOnlinePlayers().toArray().length + "&7]"));

        createLine(board, obj, "empty1", 9);
        createLine(board, obj, "profile", 8);
        createLine(board, obj, "balance", 7);
        createLine(board, obj, "empty2", 6);
        createLine(board, obj, "island", 5);
        createLine(board, obj, "level", 4);
        createLine(board, obj, "size", 3);
        createLine(board, obj, "members", 2);
        createLine(board, obj, "empty3", 1);
        createLine(board, obj, "footer", 0);

        boards.put(player.getUniqueId(), board);
        player.setScoreboard(board);
    }

    private static void createLine(Scoreboard board, Objective obj, String name, int score) {
        Team team = board.registerNewTeam(name);

        String entry = ChatColor.values()[score].toString();
        team.addEntry(entry);

        obj.getScore(entry).setScore(score);
    }

    private static void update(Player player, Island island, double balance) {
        Scoreboard board = boards.get(player.getUniqueId());
        if (board == null) return;

        setLine(board, "empty1", "", "");
        setLine(board, "profile",
                ChatColor.translateAlternateColorCodes('&', "&e&lПрофиль"), "");
        setLine(board, "balance",
                ChatColor.translateAlternateColorCodes('&', "§7&l◼ Баланс: "), formatBalance(balance));
        setLine(board, "empty2", "", "");
        setLine(board, "island",
                ChatColor.translateAlternateColorCodes('&', "&e&lОстров"), "");
        setLine(board, "level",
                ChatColor.translateAlternateColorCodes('&', "§7◼ &lУровень: "),
                ChatColor.translateAlternateColorCodes('&', "&f&l" + island.getLevel()));
        setLine(board, "size",
                ChatColor.translateAlternateColorCodes('&', "§7◼ &lРазмер: "),
                ChatColor.translateAlternateColorCodes('&', "&f&l" + island.getSize() + "x" + island.getSize()));
        setLine(board, "members",
                ChatColor.translateAlternateColorCodes('&', "§7◼ &lУчастники:"),
                ChatColor.translateAlternateColorCodes('&', " &f&l" + island.getMembers().size() + "&c/&f&l20"));
        setLine(board, "empty3", "", "");
        setLine(board, "footer", "     ",
                ChatColor.translateAlternateColorCodes('&', "&f&ldess1rous.gg"));
    }

    private static void setLine(Scoreboard board, String teamName, String prefix, String suffix) {
        Team team = board.getTeam(teamName);
        if (team == null) return;

        if (prefix.length() > 16) prefix = prefix.substring(0, 16);
        if (suffix.length() > 16) suffix = suffix.substring(0, 16);

        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }

    private static String formatBalance(double money) {
        if (money >= 1000) {
            return ChatColor.translateAlternateColorCodes('&', "&a&l" + (money / 1000) + "k&c$");
        }
        return ChatColor.translateAlternateColorCodes('&', "&a&l" + money + "&c$");
    }

    public static void updateScoreboard(IslandsCollection islandsCollection) {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (Player player: Bukkit.getOnlinePlayers()) {
                Island island = islandsCollection.getIsland(player.getUniqueId());

                if (island == null) continue;

                update(player, island, MoneyCollection.getMoney(player.getUniqueId()));
            }
        }, 20, 20);
    }
}
