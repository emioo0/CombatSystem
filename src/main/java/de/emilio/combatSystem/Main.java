package de.emilio.combatSystem;

import de.emilio.combatSystem.commands.CombatCommand;
import de.emilio.combatSystem.listeners.DamageListener;
import de.emilio.combatSystem.listeners.DeathListener;
import de.emilio.combatSystem.listeners.ProjectileListener;
import de.emilio.combatSystem.listeners.QuitListener;
import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.tasks.ActionBarTask;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ConfigManager configManager;
    private CombatManager combatManager;
    private ActionBarTask actionBarTask;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.load();

        MessageUtil.init(configManager);

        combatManager = new CombatManager(this, configManager);

        Bukkit.getPluginManager().registerEvents(new DamageListener(configManager, combatManager), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(configManager, combatManager), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(configManager, combatManager), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(configManager, combatManager), this);

        CombatCommand combatCommand = new CombatCommand(configManager, combatManager);
        if (getCommand("combat") != null) {
            getCommand("combat").setExecutor(combatCommand);
            getCommand("combat").setTabCompleter(combatCommand);
        }

        actionBarTask = new ActionBarTask(configManager, combatManager);
        actionBarTask.runTaskTimer(this, 10L, 10L); // 0.5s
    }

    @Override
    public void onDisable() {
        if (actionBarTask != null) actionBarTask.cancel();
        if (combatManager != null) combatManager.shutdown();
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }
}
