package org.achymake.worlds;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Database {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Server getServer() {
        return getWorlds().getServer();
    }

    private Message getMessage() {
        return getWorlds().getMessage();
    }

    public boolean folderExist(String worldName) {
        return new File(getServer().getWorldContainer(), worldName).exists();
    }

    public boolean worldExist(String worldName) {
        return getServer().getWorld(worldName) != null;
    }

    public World getWorld(String world) {
        return getServer().getWorld(world);
    }

    private File getFolder() {
        return new File(getWorlds().getDataFolder(), "world");
    }

    public File getFile(String worldName) {
        return new File(getFolder(), worldName + ".yml");
    }

    public File getFile(World world) {
        return getFile(world.getName());
    }

    public boolean exists(World world) {
        return getFile(world).exists();
    }

    public FileConfiguration getConfig(World world) {
        return YamlConfiguration.loadConfiguration(getFile(world));
    }

    public void setupWorlds() {
        var folder = getFolder();
        for (var files : folder.listFiles()) {
            var worldName = files.getName().replace(".yml", "");
            if (!worldExist(worldName)) {
                if (folderExist(worldName)) {
                    var config = YamlConfiguration.loadConfiguration(files);
                    getWorlds().create(worldName, World.Environment.valueOf(config.getString("environment")), config.getLong("seed"));
                } else {
                    files.delete();
                    getMessage().sendLog(Level.WARNING, worldName + " does not exist " + files.getName() + " has been deleted");
                }
            }
        }
    }

    public void createFile(World world) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", world.getName());
        config.set("display-name", world.getName());
        config.set("environment", world.getEnvironment().toString());
        config.set("seed", world.getSeed());
        config.set("portals.enable", false);
        config.set("portals.NETHER_PORTAL", "world");
        config.set("portals.END_PORTAL", "world_the_end");
        config.set("settings.pvp", true);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public boolean isPVP(World world) {
        return getConfig(world).getBoolean("settings.pvp");
    }

    public boolean isBlockBreakEnabled(World world) {
        return getConfig(world).getBoolean("settings.enable-block-break");
    }

    public boolean isBlockPlaceEnabled(World world) {
        return getConfig(world).getBoolean("settings.enable-block-place");
    }

    public boolean isInteractEnabled(World world) {
        return getConfig(world).getBoolean("settings.enable-interact");
    }

    public String getDisplayName(World world) {
        return getMessage().addColor(getConfig(world).getString("display-name"));
    }

    public void setSpawn(World world, Location location) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public Location getSpawn(World world) {
        if (getConfig(world).isConfigurationSection("spawn")) {
            var x = getConfig(world).getDouble("spawn.x");
            var y = getConfig(world).getDouble("spawn.y");
            var z = getConfig(world).getDouble("spawn.z");
            var yaw = getConfig(world).getLong("spawn.yaw");
            var pitch = getConfig(world).getLong("spawn.pitch");
            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return world.getSpawnLocation().add(0.5, 0.0, 0.5);
        }
    }

    public void setPVP(World world, boolean value) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.pvp", value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public void setBlockBreak(World world, boolean value) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.enable-block-break", value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public void setBlockPlace(World world, boolean value) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.enable-block-place", value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public void setInteract(World world, boolean value) {
        var file = getFile(world);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.enable-interact", value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }

    public boolean isPortalEnable(World world) {
        return getConfig(world).getBoolean("portals.enable");
    }

    public void teleport(Player player, String portalType) {
        player.teleport(getSpawn(getWorld(getConfig(player.getWorld()).getString("portals." + portalType))));
    }

    public void reload() {
        var folder = getFolder();
        if (folder.exists() | folder.isDirectory()) {
            for (var files : folder.listFiles()) {
                if (files.exists() | files.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(files);
                    try {
                        config.load(files);
                    } catch (IOException | InvalidConfigurationException e) {
                        getMessage().sendLog(Level.WARNING, e.getMessage());
                    }
                }
            }
        }
    }
}
