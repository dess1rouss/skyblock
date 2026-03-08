package me.dess1rous.skyblock.worlds.skyblock.island;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class PasteIslands {

    public void pasteIsland(Location loc) {
        try {
            File file = new File("plugins/WorldEdit/schematics/island.schematic");
            CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(file);
            EditSession editSession = WorldEdit.getInstance()
                    .getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), -1);

            clipboard.paste(
                    editSession,
                    new Vector(
                            loc.getBlockX(),
                            loc.getBlockY(),
                            loc.getBlockZ()
                    ),
                    false
            );

            editSession.flushQueue();
        } catch (IOException | DataException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    public static void deleteIsland(Location center, int radius) {
        try {
            BukkitWorld world = new BukkitWorld(center.getWorld());
            EditSession editSession = WorldEdit.getInstance()
                    .getEditSessionFactory().getEditSession(world, -1);

            Vector min = new Vector(
                    center.getBlockX() - radius,
                    70,
                    center.getBlockZ() - radius
            );

            Vector max = new Vector(
                    center.getBlockX() + radius,
                    150,
                    center.getBlockZ() + radius
            );
            com.sk89q.worldedit.regions.CuboidRegion region =
                    new com.sk89q.worldedit.regions.CuboidRegion(min, max);

            editSession.setBlocks(region, new com.sk89q.worldedit.blocks.BaseBlock(0));
            editSession.flushQueue();

            editSession.flushQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
