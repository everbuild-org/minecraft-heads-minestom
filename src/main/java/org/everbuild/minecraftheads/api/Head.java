package org.everbuild.minecraftheads.api;


import net.minestom.server.network.player.ResolvableProfile;

import java.util.List;
import java.util.UUID;

public interface Head {
    int getId();

    String getName();
    HeadCategory getCategory();
    UUID getUuid();
    List<HeadTag> getTags();
    String getUrlFragment();
    ResolvableProfile getHeadProfile();
}
