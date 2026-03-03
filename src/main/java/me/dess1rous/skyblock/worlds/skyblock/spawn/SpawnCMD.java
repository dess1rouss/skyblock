package me.dess1rous.skyblock.worlds.skyblock.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (player.getWorld().getName().equalsIgnoreCase("lobby")) return true;

        World spawnWorld = Bukkit.getWorld("skyblock");
        player.teleport(new Location(spawnWorld, 0.5, 60, 0.5));
        return false;
    }
}
