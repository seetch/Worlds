package org.achymake.worlds.commands;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorldsCommand implements CommandExecutor, TabCompleter {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Message getMessage() {
        return getWorlds().getMessage();
    }
    public WorldsCommand() {
        getWorlds().getCommand("worlds").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&6 " + getWorlds().name() + ":&f " + getWorlds().version());
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getWorlds().reload();
                    getMessage().send(player, "&6Worlds:&f reloaded");
                    return true;
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(consoleCommandSender, getWorlds().name() + ": " + getWorlds().version());
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getWorlds().reload();
                    getMessage().send(consoleCommandSender, "Worlds: reloaded");
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("reload");
            }
        }
        return commands;
    }
}