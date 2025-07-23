package org.everbuild.minecraftheads.request;

import net.minestom.server.codec.Codec;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface UnboundRequestFactory {
    <T> CompletableFuture<T> httpGet(RequestConfiguration config, String endpoint, Map<String, String> params, Codec<T> output);
}
