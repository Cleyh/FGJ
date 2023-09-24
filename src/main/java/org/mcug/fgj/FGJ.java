package org.mcug.fgj;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class FGJ extends JavaPlugin {

    private CheckAfk checkAfk;
    private final Map<Player, Long> afkBookings = new HashMap<>();
    private final Map<String,String> questions = new HashMap<>();
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"questions.yml"));
        List<Map<?, ?>> questionList = config.getMapList("questions");
        for (Map<?, ?> questionMap : questionList) {
            String question = (String) questionMap.get("question");
            String answer = (String) questionMap.get("answer");
            questions.put(question, answer);
        }

        this.checkAfk = new CheckAfk(afkBookings);
        Objects.requireNonNull(this.getCommand("afk")).setExecutor(new AfkCommand(afkBookings));
        getServer().getPluginManager().registerEvents(checkAfk, this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Random random = new Random();
            List<String> keys = new ArrayList<>(questions.keySet());
            String randomKey = keys.get(random.nextInt(keys.size()));
            String randomValue = questions.get(randomKey);
            checkAfk.update(new String[]{randomKey, randomValue});
        }, 0L, 5 * 60 * 20L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
