package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record MetadataModel(
        String apiVersion,
        String mode,
        String license,
        int records
) {
    public static final Codec<MetadataModel> CODEC = StructCodec.struct(
            "api_version", Codec.STRING, MetadataModel::apiVersion,
            "mode", Codec.STRING, MetadataModel::mode,
            "license", Codec.STRING, MetadataModel::license,
            "records", Codec.INT, MetadataModel::records,
            MetadataModel::new
    );

}
