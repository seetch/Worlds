package org.achymake.worlds.listeners;

import org.achymake.worlds.Database;
import org.achymake.worlds.Message;
import org.achymake.worlds.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Database getDatabase() {
        return getWorlds().getDatabase();
    }

    private Message getMessage() {
        return getWorlds().getMessage();
    }

    public PlayerInteract() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();

        if (getDatabase().isInteractEnabled(player.getWorld())) return;
        getMessage().send(player, getWorlds().getConfig().getString("messages.interact-denied"));
        event.setCancelled(true);
    }
}
