package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

public record HeadsModel(
        MetadataModel meta,
        List<String> warnings,
        List<HeadModel> heads
) {
    public static final Codec<HeadsModel> CODEC = StructCodec.struct(
            "meta", MetadataModel.CODEC, HeadsModel::meta,
            "warnings", Codec.STRING.list().optional(List.of()), HeadsModel::warnings,
            "data", HeadModel.CODEC.list(), HeadsModel::heads,
            HeadsModel::new
    );
}
