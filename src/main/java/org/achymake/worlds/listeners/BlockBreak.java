package org.achymake.worlds.listeners;

import org.achymake.worlds.Database;
import org.achymake.worlds.Message;
import org.achymake.worlds.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Database getDatabase() {
        return getWorlds().getDatabase();
    }

    private Message getMessage() {
        return getWorlds().getMessage();
    }

    public BlockBreak() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();

        if (getDatabase().isBlockBreakEnabled(player.getWorld())) return;
        getMessage().send(player, getWorlds().getConfig().getString("messages.block-break-denied"));
        event.setCancelled(true);
    }
}
