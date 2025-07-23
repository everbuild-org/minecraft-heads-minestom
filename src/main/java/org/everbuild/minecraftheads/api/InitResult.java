package org.everbuild.minecraftheads.api;

import org.everbuild.minecraftheads.models.MetadataModel;

import java.util.List;

public interface InitResult {
    boolean isSuccess();
    List<String> getWarnings();
    MetadataModel getMetadata();
}
