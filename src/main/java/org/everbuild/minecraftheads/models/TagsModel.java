package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

public record TagsModel(
        MetadataModel meta,
        List<String> warnings,
        List<TagModel> tags
) {
    public static final Codec<TagsModel> CODEC = StructCodec.struct(
            "meta", MetadataModel.CODEC, TagsModel::meta,
            "warnings", Codec.STRING.list().optional(List.of()), TagsModel::warnings,
            "data", TagModel.CODEC.list(), TagsModel::tags,
            TagsModel::new
    );
}
