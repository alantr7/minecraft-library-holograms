package com.alant7_.util.holograms.api.objects;

import org.jetbrains.annotations.NotNull;

public interface TextLine extends HologramLine {

    @NotNull String getText();

    void setText(@NotNull String text);

}
