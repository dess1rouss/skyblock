package me.dess1rous.skyblock.island;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.dess1rous.skyblock.island.IslandsLocation.getLocation;

public class IslandCreateCMD implements CommandExecutor {
    private final IslandsCollection islandsCollection;
    private final IndexCollection indexCollection;

    public IslandCreateCMD(IslandsCollection islandsCollection, IndexCollection indexCollection) {
        this.islandsCollection = islandsCollection;
        this.indexCollection = indexCollection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (islandsCollection.hasIsland(player.getUniqueId())) {
            player.sendTitle("У вас уже есть остров", "");
            return true;
        }

        int index = indexCollection.incrementIndex();

        Island island = new Island(
                player.getUniqueId(),
                getLocation(player.getWorld(), index),
                player.getName(),
                1,
                100,
                0,
                index);

        islandsCollection.save(island);

        return false;
    }
}