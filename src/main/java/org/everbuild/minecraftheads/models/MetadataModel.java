package org.everbuild.minecraftheads.models;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

public record MetadataModel(
        String apiVersion,
        boolean demoMode,
        String license,
        int records
) {
    public static final Codec<MetadataModel> CODEC = StructCodec.struct(
            "api_version", Codec.STRING, MetadataModel::apiVersion,
            "demo_mode", Codec.BOOLEAN, MetadataModel::demoMode,
            "license", Codec.STRING, MetadataModel::license,
            "records", Codec.INT, MetadataModel::records,
            MetadataModel::new
    );

}
