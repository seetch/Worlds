package org.achymake.worlds.listeners;

import org.achymake.worlds.Worlds;
import org.achymake.worlds.Database;
import org.achymake.worlds.Message;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private Worlds getWorlds() {
        return Worlds.getInstance();
    }
    private Database getDatabase() {
        return getWorlds().getDatabase();
    }
    private Message getMessage() {
        return getWorlds().getMessage();
    }
    public EntityDamageByEntity() {
        getWorlds().getManager().registerEvents(this, getWorlds());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        var entity = event.getEntity();
        var damager = event.getDamager();
        switch (damager) {
            case Arrow arrow -> {
                if (arrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target) return;
                        if (getDatabase().isPVP(target.getWorld()))return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            case Player player -> {
                if (entity instanceof Player target) {
                    if (player == target) return;
                    if (getDatabase().isPVP(target.getWorld())) return;
                    getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                    event.setCancelled(true);
                }
            }
            case Snowball snowball -> {
                if (snowball.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target)return;
                        if (getDatabase().isPVP(target.getWorld()))return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            case SpectralArrow spectralArrow -> {
                if (spectralArrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target) return;
                        if (getDatabase().isPVP(target.getWorld())) return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            case ThrownPotion thrownPotion -> {
                if (thrownPotion.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target) return;
                        if (getDatabase().isPVP(target.getWorld())) return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            case Trident trident -> {
                if (trident.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target) return;
                        if (getDatabase().isPVP(target.getWorld())) return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            case WindCharge windCharge -> {
                if (windCharge.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (player == target) return;
                        if (getDatabase().isPVP(target.getWorld())) return;
                        getMessage().send(player, "&cHey!&7 Sorry but PVP is disabled");
                        event.setCancelled(true);
                    }
                }
            }
            default -> {
            }
        }
    }
}