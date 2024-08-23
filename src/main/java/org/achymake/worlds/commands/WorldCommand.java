package org.achymake.worlds.commands;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.Database;
import org.achymake.worlds.Message;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldCommand implements CommandExecutor, TabCompleter {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Database getDatabase() {
        return getWorlds().getDatabase();
    }
    private Message getMessage() {
        return getWorlds().getMessage();
    }
    private Server getServer() {
        return getWorlds().getServer();
    }
    public WorldCommand() {
        getWorlds().getCommand("world").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    getDatabase().setSpawn(player.getWorld(), player.getLocation());
                    getMessage().send(player, player.getWorld().getName() + "&6 changed spawn point");
                    return true;
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    if (getDatabase().worldExist(player.getWorld().getName())) {
                        getDatabase().setPVP(player.getWorld(), !getDatabase().isPVP(player.getWorld()));
                        if (getDatabase().isPVP(player.getWorld())) {
                            getMessage().send(player, player.getWorld().getName() + "&6 is now pvp mode");
                        } else {
                            getMessage().send(player, player.getWorld().getName() + "&6 is no longer pvp mode");
                        }
                        return true;
                    }
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    var worldName = args[1];
                    if (getDatabase().worldExist(worldName)) {
                        player.teleport(getDatabase().getSpawn(getServer().getWorld(worldName)));
                        getMessage().send(player, "&6Teleporting to&f " + worldName);
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    var worldName = args[1];
                    if (getDatabase().worldExist(worldName)) {
                        var file = new File(getWorlds().getDataFolder(), "worlds/" + worldName + ".yml");
                        if (file.exists()) {
                            file.delete();
                        }
                        getServer().unloadWorld(worldName, true);
                        getMessage().send(player, worldName + "&6 is saved and removed");
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    if (getDatabase().worldExist(worldName)) {
                        getDatabase().setPVP(player.getWorld(), !getDatabase().isPVP(player.getWorld()));
                        if (getDatabase().isPVP(player.getWorld())) {
                            getMessage().send(player, worldName + "&6 is now pvp mode");
                        } else {
                            getMessage().send(player, worldName  + "&6 is no longer pvp mode");
                        }
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    var value = Boolean.parseBoolean(args[2]);
                    if (getDatabase().worldExist(worldName)) {
                        getDatabase().setPVP(getServer().getWorld(worldName), value);
                        if (getDatabase().isPVP(getServer().getWorld(worldName))) {
                            getMessage().send(player, worldName + "&6 is now pvp mode");
                        } else {
                            getMessage().send(player, worldName + "&6 is no longer pvp mode");
                        }
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (getDatabase().folderExist(worldName)) {
                        getMessage().send(player, worldName + "&c already exist");
                    } else {
                        getMessage().send(player, worldName + "&6 is about to be created");
                        var info = getWorlds().create(worldName, environment);
                        getMessage().send(player, info.getName() + "&6 has been created with the following:");
                        getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                        getMessage().send(player, "&6seed:&f " + info.getSeed());
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (getDatabase().folderExist(worldName)) {
                        if (getDatabase().worldExist(worldName)) {
                            getMessage().send(player, worldName + "&c already exist");
                            if (!getDatabase().getFile(worldName).exists()) {
                                getDatabase().createFile(getDatabase().getWorld(worldName));
                                getMessage().send(player, "&cFile was missing and is created");
                            }
                        } else {
                            getMessage().send(player, worldName + "&6 is about to be added");
                            var info = getWorlds().create(worldName, environment);
                            getMessage().send(player, info.getName() + "&6 has been created with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        }
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    var worldName = args[1];
                    var gamerule = args[2];
                    var value = args[3];
                    if (getDatabase().worldExist(worldName)) {
                        getServer().getWorld(worldName).setGameRuleValue(gamerule, value);
                        getMessage().send(player, worldName + "&6 changed&f " + gamerule + "&6 to&f " + value);
                    } else {
                        getMessage().send(player, worldName + "&c does not exist");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (args[3].equalsIgnoreCase("random")) {
                        if (getDatabase().folderExist(worldName)) {
                            getMessage().send(player, worldName + "&c already exist");
                        } else {
                            getMessage().send(player, worldName + "&6 is about to be created");
                            var info = getWorlds().createRandom(worldName, environment);
                            getMessage().send(player, info.getName() + "&6 has been created with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        }
                    } else {
                        var seed = Long.parseLong(args[3]);
                        if (getDatabase().folderExist(worldName)) {
                            getMessage().send(player, worldName + "&c already exist");
                        } else {
                            getMessage().send(player, worldName + "&6 is about to be created");
                            var info = getWorlds().create(worldName, environment, seed);
                            getMessage().send(player, info.getName() + "&6 has been created with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("create");
                commands.add("gamerule");
                commands.add("pvp");
                commands.add("remove");
                commands.add("setspawn");
                commands.add("teleport");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("gamerule") | args[0].equalsIgnoreCase("pvp") | args[0].equalsIgnoreCase("remove") | args[0].equalsIgnoreCase("teleport")) {
                    for (var worlds : player.getServer().getWorlds()) {
                        commands.add(worlds.getName());
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("the_end");
                }
                if (args[0].equalsIgnoreCase("create")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("the_end");
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    commands.add(String.valueOf(getDatabase().isPVP(getDatabase().getWorld(args[2]))));
                }
                if (args[0].equalsIgnoreCase("gamerule")) {
                    Collections.addAll(commands, player.getServer().getWorld(args[1]).getGameRules());
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    commands.add(player.getServer().getWorld(args[1]).getGameRuleValue(args[2]));
                }
                if (args[0].equalsIgnoreCase("create")) {
                    commands.add("random");
                }
            }
        }
        return commands;
    }
}