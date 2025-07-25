package org.everbuild.minecraftheads.api;

import java.util.List;

public record HeadCollectionImpl(
        String name,
        List<Head> heads
) implements HeadCollection {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Head> getHeads() {
        return heads;
    }
}
