package org.mcug.fgj;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class AfkCommand implements CommandExecutor {
    private final Map<Player,Long> afkBookings;

    public AfkCommand(Map<Player,Long> afkBookings) {
        this.afkBookings = afkBookings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("afk") && args.length == 2 && args[0].equalsIgnoreCase("book")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int minutes = Integer.parseInt(args[1]);
                if (minutes > 0 && minutes <= 256) {
                    afkBookings.put(player, System.currentTimeMillis() + minutes * 60 * 1000);
                    player.sendMessage("你已预定了 " + minutes + " 分钟的挂机时间。");
                } else {
                    player.sendMessage("挂机时间必须在1到256分钟之间。");
                }
            } else {
                sender.sendMessage("这个命令只能由玩家使用。");
            }
            return true;
        }
        return false;
    }

}
