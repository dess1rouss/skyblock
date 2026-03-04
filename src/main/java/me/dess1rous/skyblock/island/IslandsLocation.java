package me.dess1rous.skyblock.island;

import org.bukkit.Location;
import org.bukkit.World;

public class IslandsLocation {
    private static final int distance = 400;

    public static Location getLocationIsland(World world, int index) {

        int layer = (int) Math.ceil((Math.sqrt(index + 1) - 1) / 2);
        int legLen = layer * 2;
        int leg = (index - (2 * layer - 1) * (2 * layer - 1)) / legLen;
        int pos = (index - (2 * layer - 1) * (2 * layer - 1)) % legLen;

        int x = 0, z = 0;

        switch (leg) {
            case 0: x = layer; z = -layer + pos; break;
            case 1: x = layer - pos; z = layer; break;
            case 2: x = -layer; z = layer - pos; break;
            case 3: x = -layer + pos; z = -layer; break;
        }
        return new Location(
                world,
                x * distance,
                100,
                z * distance

        );
    }
}
