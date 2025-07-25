package org.everbuild.minecraftheads;

import net.minestom.server.entity.Player;
import org.everbuild.minecraftheads.api.Head;
import org.everbuild.minecraftheads.api.HeadCategory;
import org.everbuild.minecraftheads.api.HeadCollection;
import org.everbuild.minecraftheads.api.HeadTag;
import org.everbuild.minecraftheads.api.InitResult;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
     * Retrieves a head based on its unique identifier.
     * This method searches for and returns the corresponding {@code Head} object
     * associated with the specified {@code id}.
     *
     * @param id the unique identifier of the head to retrieve
     * @return the {@code Head} object corresponding to the specified {@code id},
     *         or {@code null} if no matching head is found
     */
    Optional<Head> getHead(int id);

    /**
     * Retrieves a head category based on its unique identifier.
     * This method searches for and returns the corresponding {@code HeadCategory} object
     * associated with the specified {@code id}.
     *
     * @param id the unique identifier of the head category to retrieve
     * @return the {@code HeadCategory} object corresponding to the specified {@code id},
     *         or {@code null} if no matching category is found
     */
    Optional<HeadCategory> getCategory(int id);

    /**
     * Retrieves a head tag based on its unique identifier.
     * This method searches for and returns the corresponding {@code HeadTag} object
     * associated with the specified {@code id}.
     *
     * @param id the unique identifier of the head tag to retrieve
     * @return the {@code HeadTag} object corresponding to the specified {@code id},
     *         or {@code null} if no matching tag is found
     */
    Optional<HeadTag> getTag(int id);

    /**
     * Retrieves the initialization result of the API.
     * The result contains metadata, warnings, and success information
     * about the initialization process.
     *
     * @return an InitResult object containing the success status, warnings, and metadata for the initialization process
     */
    InitResult getInitResult();

    /**
     * Retrieves a list of head collections associated with the license owner.
     * The head collections group multiple heads and are tied to the specific owner of the license.
     * <br>
     * The result of this should be cached user-side. It is not cached library-side for transparency
     *
     * @return a CompletableFuture that completes with a list of HeadCollection objects representing the head collections
     *         associated with the license owner
     */
    CompletableFuture<List<HeadCollection>> getHeadCollectionsByLicenseOwner();

    /**
     * Retrieves a list of head collections associated with the specified player.
     * Head collections group multiple heads and are linked to a specific player.
     * <br>
     * The result of this should be cached user-side. It is not cached library-side for transparency
     * <br>
     * Requires the Silver license!
     *
     * @param playerUUID the unique identifier of the player whose head collections are to be retrieved
     * @return a CompletableFuture that completes with a list of HeadCollection objects representing the player's head collections
     */
    CompletableFuture<List<HeadCollection>> getHeadCollectionsByPlayer(UUID playerUUID);

    /**
     * Retrieves a list of head collections associated with the given player.
     * Head collections group multiple heads and are tied to a specific player instance.
     * <br>
     * The result of this should be cached user-side. It is not cached library-side for transparency
     * <br>
     * Requires the Silver license!
     *
     * @param player the player instance whose head collections are to be retrieved
     * @return a CompletableFuture that completes with a list of HeadCollection objects associated with the specified player
     */
    CompletableFuture<List<HeadCollection>> getHeadCollectionsByPlayer(Player player);

    /**
     * Creates a new builder for the MinecraftHeads API.
     * Try to only instantiate once as caching is connected to this instance
     * @param apiKey Go to <a href="https://minecraft-heads.com/settings/api">minecraft-heads.com</a> to get an API key.
     */
    static MinecraftHeadsBuilder builder(String apiKey) {
        return new MinecraftHeadsBuilder(apiKey);
    }
}
