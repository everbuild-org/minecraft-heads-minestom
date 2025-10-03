package org.everbuild.minecraftheads.api;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class HeadImpl implements Head {
    private final int id;
    private final String name;
    private final HeadCategory category;
    private final UUID uuid;
    private final List<HeadTag> tags;
    private final String url;

    public HeadImpl(int id, String name, HeadCategory category, UUID uuid, List<HeadTag> tags, String url) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.uuid = uuid;
        this.tags = tags;
        this.url = url;
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
    public HeadCategory getCategory() {
        return category;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public List<HeadTag> getTags() {
        return tags;
    }

    @Override
    public String getUrlFragment() {
        return url;
    }

    @Override
    public ResolvableProfile getHeadProfile() {
        String jsonString = "{\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/" + url + "\"}}}";
        String base64 = Base64.getEncoder().encodeToString(jsonString.getBytes());

        return new ResolvableProfile(new GameProfile(uuid, headName(name), List.of(
                new GameProfile.Property("textures", base64, null)
        )));
    }

    private static String headName(String withIllegal) {
        String allowed = withIllegal.replaceAll("[^a-zA-Z0-9_]", "_");
        if (allowed.length() > 15) {
            allowed = allowed.substring(0, 15);
        }
        return allowed;
    }
}
