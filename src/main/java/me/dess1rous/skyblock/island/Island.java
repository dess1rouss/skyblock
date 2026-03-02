package me.dess1rous.skyblock.island;

import org.bukkit.Location;

import java.util.UUID;

public class Island {

    private UUID uuid;
    private Location location;
    private String owner;
    private int members;
    private int size;
    private int level;
    private int index;

    public Island(UUID uuid, Location location, String owner, int members, int size, int level, int index) {
        this.uuid = uuid;
        this.location = location;
        this.owner = owner;
        this.members = members;
        this.size = size;
        this.level = level;
        this.index = index;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public int getMembers() {
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
