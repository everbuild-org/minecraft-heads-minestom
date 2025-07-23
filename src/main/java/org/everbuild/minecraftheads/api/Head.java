package org.everbuild.minecraftheads.api;

import net.minestom.server.item.component.HeadProfile;

import java.util.List;
import java.util.UUID;

public interface Head {
    int getId();

    String getName();
    HeadCategory getCategory();
    UUID getUuid();
    List<HeadTag> getTags();
    String getUrlFragment();
    HeadProfile getHeadProfile();
}
