package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record PaginationModel(
        int total,
        int perPage,
        int currentPage,
        int lastPage
) {
    public static final Codec<PaginationModel> CODEC = StructCodec.struct(
            "total", Codec.INT, PaginationModel::total,
            "per_page", Codec.INT, PaginationModel::perPage,
            "current_page", Codec.INT, PaginationModel::currentPage,
            "last_page", Codec.INT, PaginationModel::lastPage,
            PaginationModel::new
    );
}
