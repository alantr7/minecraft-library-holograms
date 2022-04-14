package com.alant7_.util.holograms.objects;

import com.alant7_.util.holograms.api.objects.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CraftVisibilityManager implements VisibilityManager {

    private final CraftHologram hologram;

    private boolean isVisibleByDefault = true;

    private String visibilityPermission = null;

    private final Map<UUID, Boolean> playerSpecificVisibility = new HashMap<>();

    public CraftVisibilityManager(CraftHologram hologram) {
        this.hologram = hologram;
    }

    @Override
    public void setVisibleTo(@NotNull UUID playerId, boolean isVisible) {
        playerSpecificVisibility.put(playerId, isVisible);
    }

    @Override
    public void setVisibleTo(@NotNull Player player, boolean isVisible) {
        playerSpecificVisibility.put(player.getUniqueId(), isVisible);
        hologram.updateVisibility(player);
    }

    @Override
    public void setDefaultVisibility(boolean isVisible) {
        isVisibleByDefault = isVisible;
        Bukkit.getOnlinePlayers().forEach(hologram::updateVisibility);
    }

    @Override
    public void setVisibilityPermission(@NotNull String permission) {
        visibilityPermission = permission;
        Bukkit.getOnlinePlayers().forEach(hologram::updateVisibility);
    }

    @Override
    public void resetVisibilityPermission() {
        visibilityPermission = null;
        Bukkit.getOnlinePlayers().forEach(hologram::updateVisibility);
    }

    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        return player.getWorld() == hologram.getLocation().getWorld() && (
                playerSpecificVisibility.containsKey(player.getUniqueId())
                ? playerSpecificVisibility.get(player.getUniqueId())
                : visibilityPermission != null
                ? player.hasPermission(visibilityPermission)
                : isVisibleByDefault);
    }

    @Override
    public void resetVisibilityFor(@NotNull Player player) {
        playerSpecificVisibility.remove(player.getUniqueId());
        hologram.updateVisibility(player);
    }

    @Override
    public void resetVisibility() {
        isVisibleByDefault = true;
        playerSpecificVisibility.clear();
        visibilityPermission = null;

        Bukkit.getOnlinePlayers().forEach(hologram::updateVisibility);
    }

    @Override
    public boolean getDefaultVisibility() {
        return isVisibleByDefault;
    }

    @Override
    public @Nullable String getVisibilityPermission() {
        return visibilityPermission;
    }

}
