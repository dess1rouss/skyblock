package me.dess1rous.skyblock.island;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class Island {

    private UUID islandID;
    private Location location;
    private UUID owner;
    private List<UUID> members;
    private int size;
    private int level;
    private int index;

    public Island(UUID islandID, Location location, UUID owner, List<UUID> members, int size, int level, int index) {
        this.islandID = islandID;
        this.location = location;
        this.owner = owner;
        this.members = members;
        this.size = size;
        this.level = level;
        this.index = index;
    }

    public UUID getIslandID() {
        return islandID;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public int getSize() {
        return size;
    }

    public int getLevel() {
        return level;
    }

    public int getIndex() {
        return index;
    }
}
