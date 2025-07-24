package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public record HeadModel(
        int id,
        String name,
        int categoryId,
        @Nullable UUID uuid,
        List<Integer> tagIds,
        String url
) {
    public static final Codec<HeadModel> CODEC = StructCodec.struct(
            "id", Codec.INT, HeadModel::id,
            "n", Codec.STRING, HeadModel::name,
            "c", Codec.INT, HeadModel::categoryId,
            "i", Codec.UUID_STRING.optional(), HeadModel::uuid,
            "t", Codec.INT.list().optional(List.of()), HeadModel::tagIds,
            "u", Codec.STRING.optional(), HeadModel::url,
            HeadModel::new
    );
}
