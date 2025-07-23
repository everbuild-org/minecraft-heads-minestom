package org.everbuild.minecraftheads.request;

import net.minestom.server.codec.Codec;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BoundRequestFactory {
    private final UnboundRequestFactory requestFactory;
    private final RequestConfiguration config;

    public BoundRequestFactory(UnboundRequestFactory requestFactory, RequestConfiguration config) {
        this.requestFactory = requestFactory;
        this.config = config;
    }

    public <T> CompletableFuture<T> httpGet(String endpoint, Map<String, String> params, Codec<T> output) {
        return requestFactory.httpGet(config, endpoint, params, output);
    }
}
