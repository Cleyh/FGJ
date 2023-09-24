package org.mcug.fgj;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private HashMap<UUID, Location[]> playerLocations;

    public PlayerMoveListener() {
        this.playerLocations = new HashMap<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Location[] locations = playerLocations.getOrDefault(uuid,new Location[3]);
        System.arraycopy(locations, 1, locations, 0, 2);
        locations[2] = event.getPlayer().getLocation();
        playerLocations.put(uuid, locations);
    }

    public HashMap<UUID, Location[]> getPlayerLocations() {
        return playerLocations;
    }

}
