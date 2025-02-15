package org.achymake.worlds.listeners;

import org.achymake.worlds.Database;
import org.achymake.worlds.Message;
import org.achymake.worlds.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Database getDatabase() {
        return getWorlds().getDatabase();
    }

    private Message getMessage() {
        return getWorlds().getMessage();
    }

    public BlockPlace() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();

        if (getDatabase().isBlockPlaceEnabled(player.getWorld())) return;
        getMessage().send(player, getWorlds().getConfig().getString("messages.block-place-denied"));
        event.setCancelled(true);
    }
}
