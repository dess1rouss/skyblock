package me.dess1rous.skyblock.worlds.skyblock.island;

import java.util.UUID;

public class Invite {
    private UUID owner;

    public Invite(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }
}
