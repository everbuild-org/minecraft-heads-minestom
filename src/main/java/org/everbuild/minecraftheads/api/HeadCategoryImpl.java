package org.everbuild.minecraftheads.api;

import java.util.ArrayList;
import java.util.List;

public class HeadCategoryImpl implements HeadCategory {
    private final ArrayList<Head> heads = new ArrayList<>();
    private final int id;
    private final String name;

    public HeadCategoryImpl(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Head> getHeads() {
        return heads;
    }

    @Override
    public void addHead(Head head) {
        heads.add(head);
    }
}
