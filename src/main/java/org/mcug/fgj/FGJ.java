package org.mcug.fgj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FGJ extends JavaPlugin {

    private PlayerMoveListener playerMoveListener;
    private final Map<Player, Long> afkBookings = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.playerMoveListener = new PlayerMoveListener(afkBookings);
        this.getCommand("afk").setExecutor(new AfkCommand(afkBookings));
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                playerMoveListener.checkAFK();
            }
        },0L,5*60*20L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
