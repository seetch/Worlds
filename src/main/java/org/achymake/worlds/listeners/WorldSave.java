package org.achymake.worlds.listeners;

import org.achymake.worlds.Database;
import org.achymake.worlds.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldSave implements Listener {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Database getDatabase() {
        return getWorlds().getDatabase();
    }
    public WorldSave() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldSave(WorldSaveEvent event) {
        if (getDatabase().getFile(event.getWorld()).exists())return;
        getDatabase().createFile(event.getWorld());
    }
}