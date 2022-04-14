package com.alant7_.util.holograms.api.objects;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.holograms.api.enums.LinesAlignment;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public interface Hologram {

    @NotNull UUID getIdentifier();

    @NotNull AlanJavaPlugin getPlugin();

    @NotNull Location getLocation();

    void setLocation(@NotNull Location location);

    @NotNull TextLine addLine(@NotNull String text);

    HologramLine addLine(@NotNull ItemStack item);

    HologramLine getLineAt(int index);

    HologramLine[] getLines();

    int getLinesCount();

    void removeLine(int index);

    void removeLine(@NotNull HologramLine line);

    void removeLines();

    @NotNull VisibilityManager getVisibilityManager();

    @NotNull LinesAlignment getAlignment();

    void setAlignment(LinesAlignment alignment);

    /**
     * @apiNote Requires ProtocolLib!
     * @param clickListener Invoked when a player clicks the hologram
     */
    void addInteractionListener(Consumer<Player> clickListener);

    void removeInteractionListener(Consumer<Player> clickListener);

    void clearInteractionListeners();

    void delete();

}
