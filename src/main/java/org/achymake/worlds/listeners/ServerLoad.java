package org.achymake.worlds.listeners;

import org.achymake.worlds.Database;
import org.achymake.worlds.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoad implements Listener {

    private Worlds getWorlds() {
        return Worlds.getInstance();
    }

    private Database getDatabase() {
        return getWorlds().getDatabase();
    }

    public ServerLoad() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerLoad(ServerLoadEvent event) {
        if (!event.getType().equals(ServerLoadEvent.LoadType.STARTUP)) return;
        getDatabase().setupWorlds();
    }
}
