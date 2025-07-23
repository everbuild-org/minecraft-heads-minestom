package org.everbuild.minecraftheads;

import org.everbuild.minecraftheads.api.InitResult;
import org.everbuild.minecraftheads.models.MetadataModel;

import java.util.List;

public record InitResultImpl(
        boolean isSuccess,
        List<String> warnings,
        MetadataModel metadata
) implements InitResult {
    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public List<String> getWarnings() {
        return warnings;
    }

    @Override
    public MetadataModel getMetadata() {
        return metadata;
    }
}
