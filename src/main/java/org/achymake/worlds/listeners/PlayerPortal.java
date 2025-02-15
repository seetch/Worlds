package org.achymake.worlds.listeners;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Database getDatabase() {
        return getWorlds().getDatabase();
    }

    public PlayerPortal() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPortal(PlayerPortalEvent event) {
        var player = event.getPlayer();
        if (!getDatabase().isPortalEnable(player.getWorld())) return;
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
            event.setCancelled(true);
            getDatabase().teleport(player, "NETHER_PORTAL");
        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            event.setCancelled(true);
            getDatabase().teleport(player, "END_PORTAL");
        }
    }
}
