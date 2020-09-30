package com.shanebeestudios.domo;

import com.shanebeestudios.domo.command.GiveCmd;
import com.shanebeestudios.domo.config.PlayerDataConfig;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.listener.EntityListener;
import com.shanebeestudios.domo.listener.PlayerListener;
import com.shanebeestudios.domo.listener.WorldListener;
import com.shanebeestudios.domo.manager.PlayerManager;
import com.shanebeestudios.domo.manager.RecipeManager;
import com.shanebeestudios.domo.task.EnergyTask;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class DomoBloot extends JavaPlugin {

    static {
        // Register PlayerData configuration serialization
        ConfigurationSerialization.registerClass(PlayerData.class, "PlayerData");
    }

    private static DomoBloot instance;
    private PlayerDataConfig playerDataConfig;
    private PlayerManager playerManager;
    private RecipeManager recipeManager;

    // Tasks
    private EnergyTask energyTask;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        this.playerDataConfig = new PlayerDataConfig(this);
        this.playerManager = new PlayerManager(this);
        this.recipeManager = new RecipeManager(this);

        loadListeners();
        loadCommands();
        loadTasks();
        Util.setGameRules();

        Util.log("Successfully enabled plugin in &b" + (System.currentTimeMillis() - start) + " &7milliseconds");
    }

    @Override
    public void onDisable() {
        this.playerManager.getPlayerDatas().forEach(playerData -> playerDataConfig.saveData(playerData));
        this.energyTask.cancel();
        this.energyTask = null;
        this.playerDataConfig = null;
        this.playerManager = null;
        instance = null;
    }

    private void loadListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new WorldListener(), this);
        pm.registerEvents(new EntityListener(this), this);
    }

    private void loadTasks() {
        this.energyTask = new EnergyTask(this);
    }

    private void loadCommands() {
        new GiveCmd(this);
    }

    public static DomoBloot getPlugin() {
        return instance;
    }

    public PlayerDataConfig getPlayerDataConfig() {
        return playerDataConfig;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

}