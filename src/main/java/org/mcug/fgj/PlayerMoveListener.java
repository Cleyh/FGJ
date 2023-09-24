package org.mcug.fgj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener{

    private HashMap<UUID, Location[]> playerLocations;
    private Map<Player, Long> afkBookings;

    public PlayerMoveListener(Map<Player, Long> afkBookings) {
        this.afkBookings = afkBookings;
        this.playerLocations = new HashMap<>();
    }

    public void checkAFK(){

        for(Player player : Bukkit.getOnlinePlayers()) {

            if(isAfkBooked(player)) continue;

            Location[] locations = playerLocations.computeIfAbsent(player.getUniqueId(), k -> new Location[3]);

            //there can cover Out-of-date data easily.
            System.arraycopy(locations, 1, locations, 0, 2);
            //put the current locations into locations[2].
            locations[2] = player.getLocation();

            if(isAFK(locations)) {

                player.sendMessage("You’re in AFK");
                //AFK to do

            }
        }
    }

    private boolean isAfkBooked(Player player) {
        return afkBookings.containsKey(player) && System.currentTimeMillis()<afkBookings.get(player);
    }

    private boolean isAFK(Location[] locations) {
        if (locations[0] == null || locations[1] == null || locations[2] == null) return false;
        double maxDistanceSquared = 16.0000;
        return locations[0].distanceSquared(locations[1]) <= maxDistanceSquared && locations[1].distanceSquared(locations[2]) <= maxDistanceSquared;
    }

    public HashMap<UUID, Location[]> getPlayerLocations() {
        return playerLocations;
    }
}
