package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

public record CategoriesModel(
        MetadataModel meta,
        List<String> warnings,
        List<CategoryModel> categories
) {
    public static final Codec<CategoriesModel> CODEC = StructCodec.struct(
            "meta", MetadataModel.CODEC, CategoriesModel::meta,
            "warnings", Codec.STRING.list().optional(List.of()), CategoriesModel::warnings,
            "data", CategoryModel.CODEC.list(), CategoriesModel::categories,
            CategoriesModel::new
    );
}
