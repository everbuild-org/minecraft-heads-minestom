package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

public record CollectionModel(
        String name,
        List<Integer> heads
) {
    public static final Codec<CollectionModel> CODEC = StructCodec.struct(
            "n", Codec.STRING, CollectionModel::name,
            "h", Codec.INT.list(), CollectionModel::heads,
            CollectionModel::new
    );
}
