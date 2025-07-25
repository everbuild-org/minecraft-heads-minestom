package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

public record CollectionsModel(
        MetadataModel meta,
        List<String> warnings,
        List<CollectionModel> collections
) {
    public static final Codec<CollectionsModel> CODEC = StructCodec.struct(
            "meta", MetadataModel.CODEC, CollectionsModel::meta,
            "warnings", Codec.STRING.list().optional(List.of()), CollectionsModel::warnings,
            "data", CollectionModel.CODEC.list(), CollectionsModel::collections,
            CollectionsModel::new
    );
}
