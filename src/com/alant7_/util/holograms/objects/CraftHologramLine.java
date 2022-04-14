package com.alant7_.util.holograms.objects;

import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.api.objects.HologramLine;
import com.alant7_.util.holograms.objects.nms.HologramLineNMS;
import com.alant7_.util.holograms.objects.nms.v1_18;
import com.alant7_.util.holograms.objects.nms.v1_18_R2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class CraftHologramLine implements HologramLine {

    private final Hologram hologram;

    protected Location location;

    protected final HologramLineNMS<?, ?, ?, ?> handle;

    public CraftHologramLine(Hologram hologram, Location location) {
        this.hologram = hologram;

        this.location = location;
        String version = Bukkit.getBukkitVersion().split("\\-")[0];

        switch (version) {
            case "1.18.2":
                handle = new v1_18_R2(this);
                break;
            case "1.18.1":
            case "1.18":
                handle = new v1_18(this);
                break;
            default:
                handle = null;
                break;
        }

        assert handle != null;
        ((CraftHologramManager) hologram.getPlugin().getHologramManager()).registerInteractableHologram(handle.getSlimeId(), hologram);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (hologram.getVisibilityManager().isVisibleTo(player))
                handle.createLine(player);
        });
    }

    @Override
    public @NotNull Hologram getHologram() {
        return hologram;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        handle.setLocation(location);
    }

    @Override
    public void remove() {
        hologram.removeLine(this);
        ((CraftHologramManager) hologram.getPlugin().getHologramManager()).unregisterInteractableHologram(handle.getSlimeId());
        Bukkit.getOnlinePlayers().forEach(handle::removeLine);
    }

}
