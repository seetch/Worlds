package org.achymake.worlds;

import org.achymake.worlds.commands.WorldCommand;
import org.achymake.worlds.commands.WorldsCommand;
import org.achymake.worlds.listeners.*;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public final class Worlds extends JavaPlugin {
    private static Worlds instance;
    private static Database database;
    private static Message message;
    private static UpdateChecker updateChecker;
    @Override
    public void onEnable() {
        instance = this;
        message = new Message();
        database = new Database();
        updateChecker = new UpdateChecker();
        commands();
        events();
        reload();
        new PlaceholderProvider().register();
        getMessage().sendLog(Level.INFO, "Enabled " + name() + " " + version());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        new PlaceholderProvider().unregister();
        getServer().getScheduler().cancelTasks(this);
        getMessage().sendLog(Level.INFO, "Disabled " + name() + " " + version());
    }
    private void commands() {
        new WorldsCommand();
        new WorldCommand();
    }
    private void events() {
        new EntityDamageByEntity();
        new PlayerJoin();
        new PlayerPortal();
        new ServerLoad();
        new WorldLoad();
        new WorldSave();
    }
    public void reload() {
        var file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
        getDatabase().reload();
    }
    public PluginManager getManager() {
        return getServer().getPluginManager();
    }
    public BukkitScheduler getScheduler() {
        return getServer().getScheduler();
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public Message getMessage() {
        return message;
    }
    public static Worlds getInstance() {
        return instance;
    }
    public Database getDatabase() {
        return database;
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    private WorldCreator getCreator(String worldName) {
        return new WorldCreator(worldName);
    }
    public WorldInfo create(String worldName, World.Environment environment) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        return creator.createWorld();
    }
    public WorldInfo create(String worldName, World.Environment environment, long seed) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        creator.seed(seed);
        return creator.createWorld();
    }
    public WorldInfo createRandom(String worldName, World.Environment environment) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        creator.seed(new Random().nextLong());
        return creator.createWorld();
    }
}