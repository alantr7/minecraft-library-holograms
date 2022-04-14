package com.alant7_.util.holograms.serializers;

import com.alant7_.util.TriConsumer;
import com.alant7_.util.data.serialization.FieldSerializer;
import com.alant7_.util.holograms.api.objects.HologramLine;
import com.alant7_.util.holograms.api.objects.TextLine;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.BiFunction;

public class HologramLineSerializer extends FieldSerializer<HologramLine> {

    public HologramLineSerializer() {
        super((section, key, line) -> {

            if (line instanceof TextLine textLine) {
                section.set(key + ".Text", textLine.getText());
            }

        }, (section, key) -> {

            return null;

        });
    }

}
