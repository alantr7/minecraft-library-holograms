package com.alant7_.util.holograms.objects;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.data.serialization.DataType;
import com.alant7_.util.data.serialization.SerializableField;
import com.alant7_.util.holograms.api.enums.LinesAlignment;
import com.alant7_.util.holograms.api.objects.*;
import com.alant7_.util.holograms.serializers.HologramLineSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class CraftHologram implements Hologram {

    private final AlanJavaPlugin plugin;

    private final UUID identifier;

    private Location location;

    private final List<HologramLine> lines = new LinkedList<>();

    private final List<Consumer<Player>> interactionListeners = new LinkedList<>();

    private final VisibilityManager visibilityManager;

    private LinesAlignment alignment = LinesAlignment.TOP;

    public CraftHologram(AlanJavaPlugin plugin, Location location) {
        this.plugin = plugin;
        this.identifier = UUID.randomUUID();
        this.location = location;
        this.visibilityManager =  new CraftVisibilityManager(this);
    }

    @NotNull
    @Override
    public AlanJavaPlugin getPlugin() {
        return plugin;
    }

    @NotNull
    @Override
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull Location location) {
        if (this.location != null && this.location.getWorld() != location.getWorld()) {
            this.location = location;
            HologramLine[] lines = getLines();
            removeLines();

            for (HologramLine line : lines)
                addLine(line);
        } else {
            this.location = location;
            updateLocations();
        }
    }

    private void updateLocations() {
        double offsetY = alignment == LinesAlignment.TOP
                ? 0
                : alignment == LinesAlignment.BOTTOM
                ? -lines.size() * 0.32
                : -lines.size() / 2f * 0.32 + 0.16;
        for (var line : lines) {
            ((CraftHologramLine) line).setLocation(location.clone().subtract(0, offsetY, 0));
            offsetY += 0.32;
        }
    }

    @Override
    public @NotNull TextLine addLine(@NotNull String text) {
        double offsetY = alignment == LinesAlignment.TOP
                ? 0
                : alignment == LinesAlignment.BOTTOM
                ? -lines.size() * 0.32
                : -lines.size() * 0.32 / 2;

        TextLine line = new CraftTextLine(this, location.clone().subtract(0, offsetY + lines.size() * 0.32, 0));
        line.setText(text);
        lines.add(line);

        updateLocations();
        return line;
    }

    @Override
    public HologramLine addLine(@NotNull ItemStack item) {
        return null;
    }

    private void addLine(HologramLine line) {
        if (line instanceof TextLine) {
            addLine(((TextLine) line).getText());
        }
    }

    @Override
    public HologramLine getLineAt(int index) {
        if (index >= 0 && index < lines.size())
            return lines.get(index);

        return null;
    }

    @Override
    public HologramLine[] getLines() {
        return lines.toArray(HologramLine[]::new);
    }

    @Override
    public int getLinesCount() {
        return lines.size();
    }

    @Override
    public void removeLine(int index) {
        if (index >= 0 && index < lines.size()) {
            lines.remove(index).remove();
            updateLocations();
        }
    }

    @Override
    public void removeLine(@NotNull HologramLine line) {
        removeLine(lines.indexOf(line));
    }

    @Override
    public void removeLines() {
        for (HologramLine line : getLines())
            line.remove();

        lines.clear();
    }

    public void send(@NotNull Player player) {
        if (visibilityManager.isVisibleTo(player)) {
            lines.forEach(line -> {
                ((CraftHologramLine) line).handle.createLine(player);
                ((CraftHologramLine) line).handle.updateLine(player);
            });
        }
    }

    public void update(@NotNull Player player) {
        if (visibilityManager.isVisibleTo(player)) {
            lines.forEach(line -> ((CraftHologramLine) line).handle.updateLine(player));
        } else {
            destroy(player);
        }
    }

    public void destroy(@NotNull Player player) {
        lines.forEach(line -> ((CraftHologramLine) line).handle.removeLine(player));
    }

    public void destroy() {
        Bukkit.getOnlinePlayers().forEach(this::destroy);
    }

    public void updateVisibility(@NotNull Player player) {
        if (visibilityManager.isVisibleTo(player))
            send(player);
        else destroy(player);
    }

    @Override
    public @NotNull VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    @Override
    public @NotNull LinesAlignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(LinesAlignment alignment) {
        this.alignment = alignment;
        updateLocations();
    }

    @Override
    public void addInteractionListener(Consumer<Player> clickListener) {
        interactionListeners.add(clickListener);
    }

    public boolean isInteractable() {
        return interactionListeners.size() > 0;
    }

    @Override
    public void removeInteractionListener(Consumer<Player> clickListener) {
        interactionListeners.remove(clickListener);
    }

    @Override
    public void clearInteractionListeners() {
        interactionListeners.clear();
    }

    public void notifyInteractionListeners(@NotNull Player player) {
        if (Bukkit.isPrimaryThread()) {
            interactionListeners.forEach(c -> c.accept(player));
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                interactionListeners.forEach(c -> c.accept(player));
            });
        }
    }

    @Override
    public void delete() {
        plugin.getHologramManager().deleteHologram(this);
    }

}
