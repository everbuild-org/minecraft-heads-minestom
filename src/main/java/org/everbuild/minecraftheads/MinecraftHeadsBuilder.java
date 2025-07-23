package org.everbuild.minecraftheads;

import org.everbuild.minecraftheads.request.HttpClientRequestFactoryImpl;
import org.everbuild.minecraftheads.request.UnboundRequestFactory;

import java.util.concurrent.CompletableFuture;

public class MinecraftHeadsBuilder {
    private final String apiKey;
    private boolean isDemo = false;
    private boolean useTags = false;
    private UnboundRequestFactory requestFactory = new HttpClientRequestFactoryImpl();
    private String baseUrl = "https://minecraft-heads.com/api";

    public MinecraftHeadsBuilder(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Use the <a href="https://minecraft-heads.com/wiki/minecraft-heads/api-v2-for-developers">demo mode</a> of the API
     * @param isDemo enable demo mode
     * @return the current instance of MinecraftHeadsBuilder for method chaining
     */
    public MinecraftHeadsBuilder demo(boolean isDemo) {
        this.isDemo = isDemo;
        return this;
    }

    /**
     * Sets the request factory to be used for creating HTTP requests.
     * This allows customization of the underlying HTTP request behavior.
     *
     * @param requestFactory an implementation of the RequestFactory interface
     * @return the current instance of MinecraftHeadsBuilder for method chaining
     */
    public MinecraftHeadsBuilder requestFactory(UnboundRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        return this;
    }

    /**
     * Enables or disables the inclusion of tags in API responses.
     * This allows you to specify whether the retrieved Minecraft head data should include tag information.
     * Requires API Silver tier or higher!
     *
     * @param useTags whether to include tags in the API response
     * @return the current instance of MinecraftHeadsBuilder for method chaining
     */
    public MinecraftHeadsBuilder tags(boolean useTags) {
        this.useTags = useTags;
        return this;
    }

    /**
     * Sets the base URL for the API. This allows using a custom endpoint instead of the default one.
     * The default is <a href="https://minecraft-heads.com/api">https://minecraft-heads.com/api</a> and
     * replacements are expected to follow the same format
     *
     * @param baseUrl the base URL to use for API requests
     * @return the current instance of MinecraftHeadsBuilder for method chaining
     */
    public MinecraftHeadsBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public CompletableFuture<MinecraftHeads> build() {
        // Identifier for the library, not the user
        String appId = "7a7109ad-1a31-43c9-806e-20b4910ed351";
        MinecraftHeadsImpl impl = new MinecraftHeadsImpl(
                apiKey,
                appId,
                isDemo,
                requestFactory,
                baseUrl,
                useTags
        );

        return impl.init().thenApply(_ -> impl);
    }
}
