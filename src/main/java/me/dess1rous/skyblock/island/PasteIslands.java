package me.dess1rous.skyblock.island;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class PasteIslands {

    public void pasteIsland(Location loc) {
        try {
            File file = new File("plugins/WorldEdit/schematics/island.schematic");
            Clipboard clipboard = (Clipboard) SchematicFormat.MCEDIT.load(file);
            BukkitWorld worldWE = new BukkitWorld(loc.getWorld());

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldWE, -1);

            com.sk89q.worldedit.world.registry.WorldData worldData =
                    worldWE.getWorldData();
            Operation operation = new ClipboardHolder(clipboard, worldData).createPaste(editSession, worldData)
                    .to(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
                    .ignoreAirBlocks(false).build();

            Operations.complete(operation);
            editSession.flushQueue();
        } catch (IOException | DataException | WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static void deleteIsland(Location center, int radius) {
        try {
            BukkitWorld worldWE = new BukkitWorld(center.getWorld());

            EditSession editSession = WorldEdit.getInstance()
                    .getEditSessionFactory().getEditSession(worldWE, -1);

            Vector min = new Vector(
                    center.getBlockX() - radius,
                    0,
                    center.getBlockZ() - radius
            );

            Vector max = new Vector(
                    center.getBlockX() + radius,
                    255,
                    center.getBlockZ() + radius
            );
            com.sk89q.worldedit.regions.CuboidRegion region =
                    new com.sk89q.worldedit.regions.CuboidRegion(min, max);

            com.sk89q.worldedit.blocks.BaseBlock air =
                    new com.sk89q.worldedit.blocks.BaseBlock(0);

            editSession.setBlocks(region, air);
            editSession.flushQueue();

            editSession.flushQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
