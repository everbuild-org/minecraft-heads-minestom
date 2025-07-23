package org.everbuild.minecraftheads.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Transcoder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClientRequestFactoryImpl implements UnboundRequestFactory {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final Gson gson = new Gson();

    @Override
    public <T> CompletableFuture<T> httpGet(RequestConfiguration config, String endpoint, Map<String, String> params, Codec<T> output) {
        Map<String, String> allParams = new HashMap<>(Map.of("app_uuid", config.appId()));
        allParams.putAll(params);
        String endpointWithParams = endpoint + "?" +
                String.join(
                        "&",
                        allParams
                                .entrySet()
                                .stream()
                                .map(e ->
                                        URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                                                URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)
                                )
                                .toList()
                );
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointWithParams))
                .header("api-key", config.license())
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return responseFuture.thenApply(response -> {
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            return output.decode(Transcoder.JSON, jsonObject).orElseThrow("Could not decode response");
        });
    }
}
