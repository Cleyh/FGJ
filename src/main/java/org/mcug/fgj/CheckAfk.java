package org.mcug.fgj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CheckAfk implements Listener {

    private HashMap<UUID, Location[]> playerLocations;
    private Map<Player, Long> afkBookings;
    private String[] currentQuestion = null;
    private String[] lastQuestion = null;
    private final Map<Player, String> playerAnswers = new HashMap<>();

    public CheckAfk(Map<Player, Long> afkBookings) {
        this.afkBookings = afkBookings;
        this.playerLocations = new HashMap<>();
    }

    public void update(String[] questions){

        lastQuestion = currentQuestion;
        currentQuestion = questions;

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(isAfkBooked(player)) continue;
            if(!isAnswer(player)) kickPlayer(player);

            //update locations
            Location[] locations = playerLocations.computeIfAbsent(player.getUniqueId(), k -> new Location[3]);
            //there can cover Out-of-date data easily.
            System.arraycopy(locations, 1, locations, 0, 2);
            //put the current locations into locations[2].
            locations[2] = player.getLocation();

            //todo
            if(isAFK((locations))) todoAfk(player);
        }
    }

    private void todoAfk(Player player) {
        //AFK to do
        player.sendTitle("挂机判定","请在5分钟内执行以下操作，否则将被系统判定为挂机" ,10, 70, 20) ;
        player.sendMessage("请在5分钟内执行以下操作，否则将被系统判定为挂机");
        askQuestion(player);
    }

    private boolean isAnswer(Player player) {
        if(!playerAnswers.containsKey(player)) return true;
        else {
            String answer = playerAnswers.get(player);

            if(answer != null && answer.equals(lastQuestion[1])) {
                this.playerAnswers.remove(player);
                return true;
            }
        }
        return false;
    }

    private void askQuestion(Player player) {
        playerAnswers.put(player, null);
        player.sendTitle("请在聊天窗回答下面的问题",currentQuestion[0], 10, 70, 20);
        player.sendMessage(currentQuestion[0]);
    }

    private void kickPlayer(Player player) {
        player.kickPlayer("You have been kicked for AFK.");
        playerAnswers.remove(player);
    }


    private boolean isAfkBooked(Player player) {
        return afkBookings.containsKey(player) && System.currentTimeMillis()<afkBookings.get(player);
    }

    private boolean isAFK(Location[] locations) {
        if (locations[0] == null || locations[1] == null || locations[2] == null) return false;
        double maxDistanceSquared = 16.0000;
        return locations[0].distanceSquared(locations[1]) <= maxDistanceSquared && locations[1].distanceSquared(locations[2]) <= maxDistanceSquared;
    }

    @EventHandler
    public void onPlayerAnswer(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (playerAnswers.containsKey(player)){
            String message = event.getMessage();
            if (message.equals(currentQuestion[1])) {
                playerAnswers.put(player, message);
                player.sendMessage("已完成挂机判定！");
            }
        }
    }
}
