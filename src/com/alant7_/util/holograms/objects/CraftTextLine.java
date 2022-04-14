package com.alant7_.util.holograms.objects;

import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.api.objects.TextLine;
import com.alant7_.util.holograms.objects.nms.HologramLineNMS;
import com.alant7_.util.holograms.objects.nms.v1_18;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class CraftTextLine extends CraftHologramLine implements TextLine {

    private String text;

    public CraftTextLine(Hologram hologram, Location location) {
        super(hologram, location);
        setText("");
    }

    @Override
    public @NotNull String getText() {
        return text;
    }

    @Override
    public void setText(@NotNull String text) {
        this.text = text;
        Bukkit.getOnlinePlayers().forEach(handle::updateLine);
    }

}
