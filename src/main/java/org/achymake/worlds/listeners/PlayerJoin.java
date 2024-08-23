package org.achymake.worlds.listeners;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private UpdateChecker getUpdateChecker() {
        return getWorlds().getUpdateChecker();
    }
    public PlayerJoin() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        getUpdateChecker().getUpdate(event.getPlayer());
    }
}