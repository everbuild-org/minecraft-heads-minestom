package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record CategoryModel(
        int id,
        String name
) {
    public static final Codec<CategoryModel> CODEC = StructCodec.struct(
            "id", Codec.INT, CategoryModel::id,
            "n", Codec.STRING, CategoryModel::name,
            CategoryModel::new
    );
}
