package org.achymake.worlds;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlaceholderProvider extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "worlds";
    }

    @Override
    public String getAuthor() {
        return "AchyMake";
    }

    @Override
    public String getVersion() {
        return Worlds.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean register() {
        return super.register();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        } else {
            switch (params) {
                case "name" -> {
                    return Worlds.getInstance().getDatabase().getConfig(player.getWorld()).getString("name");
                }
                case "display_name" -> {
                    return Worlds.getInstance().getDatabase().getDisplayName(player.getWorld());
                }
                case "pvp" -> {
                    return String.valueOf(Worlds.getInstance().getDatabase().getConfig(player.getWorld()).getBoolean("pvp"));
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
