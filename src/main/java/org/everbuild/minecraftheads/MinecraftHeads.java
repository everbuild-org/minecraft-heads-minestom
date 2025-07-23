package org.everbuild.minecraftheads;

import org.everbuild.minecraftheads.api.Head;
import org.everbuild.minecraftheads.api.HeadCategory;
import org.everbuild.minecraftheads.api.HeadTag;
import org.everbuild.minecraftheads.api.InitResult;

import java.util.List;

public interface MinecraftHeads {
    /**
     * Retrieves a list of head categories available in the API.
     * Each category represents a grouping of custom heads within the service.
     *
     * @return a list of HeadCategory objects representing the available categories
     */
    List<HeadCategory> getCategories();

    /**
     * Retrieves a list of head tags available in the API.
     * Each tag represents a grouping or categorization of custom heads within the service.
     * <br>
     * Only works with MinecraftHeadsBuilder#tags enabled, thus requiring API Silver tier or higher.
     *
     * @return a list of HeadTag objects representing the available tags
     */
    List<HeadTag> getTags();

    /**
     * Retrieves a list of custom heads available from the API.
     * Custom heads are Minecraft head objects with specific attributes,
     * categories, tags, and associated metadata.
     *
     * @return a list of Head objects representing the available custom heads
     */
    List<Head> getCustomHeads();

    /**
     * Retrieves the initialization result of the API.
     * The result contains metadata, warnings, and success information
     * about the initialization process.
     *
     * @return an InitResult object containing the success status, warnings, and metadata for the initialization process
     */
    InitResult getInitResult();

    /**
     * Creates a new builder for the MinecraftHeads API.
     * Try to only instantiate once as caching is connected to this instance
     * @param apiKey Go to <a href="https://minecraft-heads.com/settings/api">minecraft-heads.com</a> to get an API key.
     */
    static MinecraftHeadsBuilder builder(String apiKey) {
        return new MinecraftHeadsBuilder(apiKey);
    }
}
