package org.achymake.worlds.listeners;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoad implements Listener {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Database getDatabase() {
        return getWorlds().getDatabase();
    }
    public WorldLoad() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        if (getDatabase().getFile(event.getWorld()).exists())return;
        getDatabase().createFile(event.getWorld());
    }
}