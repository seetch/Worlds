package org.achymake.worlds;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateChecker {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Message getMessage() {
        return getWorlds().getMessage();
    }
    private BukkitScheduler getScheduler() {
        return getWorlds().getScheduler();
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            if (player.hasPermission("worlds.event.join.update")) {
                getScheduler().runTaskLater(getWorlds(), new Runnable() {
                    @Override
                    public void run() {
                        getLatest((latest) -> {
                            if (!getWorlds().version().equals(latest)) {
                                getMessage().send(player, getWorlds().name() + "&6 has new update:");
                                getMessage().send(player, "-&a https://www.spigotmc.org/resources/106196/");
                            }
                        });
                    }
                }, 5);
            }
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getScheduler().runTaskAsynchronously(getWorlds(), new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        if (!getWorlds().version().equals(latest)) {
                            getMessage().sendLog(Level.INFO, getWorlds().name() + " has new update:");
                            getMessage().sendLog(Level.INFO, "- https://www.spigotmc.org/resources/106196/");
                        }
                    });
                }
            });
        }
    }
    public void getLatest(Consumer<String> consumer) {
        try (var inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 106196).openStream()) {
            var scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            } else {
                inputStream.close();
            }
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    private boolean notifyUpdate() {
        return getWorlds().getConfig().getBoolean("notify-update");
    }
}