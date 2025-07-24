package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record HeadsModel(
        MetadataModel meta,
        List<String> warnings,
        List<HeadModel> heads,
        @Nullable PaginationModel pagination
) {
    public static final Codec<HeadsModel> CODEC = StructCodec.struct(
            "meta", MetadataModel.CODEC, HeadsModel::meta,
            "warnings", Codec.STRING.list().optional(List.of()), HeadsModel::warnings,
            "data", HeadModel.CODEC.list(), HeadsModel::heads,
            "pagination", PaginationModel.CODEC.optional(), HeadsModel::pagination,
            HeadsModel::new
    );
}
