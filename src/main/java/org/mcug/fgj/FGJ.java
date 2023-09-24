package org.mcug.fgj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class FGJ extends JavaPlugin {

    private PlayerMoveListener playerMoveListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.playerMoveListener = new PlayerMoveListener();
        Bukkit.getPluginManager().registerEvents(playerMoveListener, this);
        Bukkit.getScheduler().runTaskTimer(this, this::checkAFK, 20L * 60 * 5, 20L * 60 * 5);

    }

    private void checkAFK() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isAFK(player)) {
                player.kickPlayer("你已被判定为挂机并被踢出服务器！");
            }
        }
    }

    private boolean isAFK(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerMoveListener.getPlayerLocations().containsKey(uuid)) {
            return false;
        }
        Location[] locations = playerMoveListener.getPlayerLocations().get(uuid);
        for (int i = 0; i < locations.length - 1; i++) {
            if (locations[i] == null || locations[i + 1] == null) {
                return false;
            }
            if (locations[i].distance(locations[i + 1]) > 4) { // 如果玩家移动距离大于4，则判定其不在挂机
                return false;
            }
        }
        return true; // 如果玩家连续3次位置变动都在4*4范围内，则判定其在挂机
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
