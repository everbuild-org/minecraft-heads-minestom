package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record TagModel(
        int id,
        String name
) {
    public static final Codec<TagModel> CODEC = StructCodec.struct(
            "id", Codec.INT, TagModel::id,
            "n", Codec.STRING, TagModel::name,
            TagModel::new
    );
}
