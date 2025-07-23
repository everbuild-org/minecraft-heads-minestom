package org.everbuild.minecraftheads.api;

import java.util.List;

public interface HeadTag {
    int getId();
    String getName();
    List<Head> getHeads();

    void addHead(Head head);
}
