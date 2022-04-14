package com.alant7_.util.holograms.api;

import com.alant7_.util.holograms.api.objects.Hologram;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface HologramManager {

    @NotNull Hologram createHologram(@NotNull Location location);

    @NotNull Hologram[] getHolograms();

    void deleteHolograms();

    void deleteHologram(@NotNull Hologram hologram);

}
