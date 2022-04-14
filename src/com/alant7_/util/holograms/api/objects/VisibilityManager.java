package com.alant7_.util.holograms.api.objects;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface VisibilityManager {

    void setVisibleTo(@NotNull UUID playerId, boolean isVisible);

    void setVisibleTo(@NotNull Player player, boolean isVisible);

    void setDefaultVisibility(boolean isVisible);

    void setVisibilityPermission(@NotNull String permission);

    void resetVisibilityPermission();

    boolean isVisibleTo(@NotNull Player player);

    void resetVisibilityFor(@NotNull Player player);

    void resetVisibility();

    boolean getDefaultVisibility();

    @Nullable String getVisibilityPermission();

}
