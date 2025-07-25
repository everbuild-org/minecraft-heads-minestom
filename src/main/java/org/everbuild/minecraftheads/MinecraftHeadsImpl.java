package org.everbuild.minecraftheads;

import net.minestom.server.entity.Player;
import org.everbuild.minecraftheads.api.Head;
import org.everbuild.minecraftheads.api.HeadCategory;
import org.everbuild.minecraftheads.api.HeadCategoryImpl;
import org.everbuild.minecraftheads.api.HeadCollection;
import org.everbuild.minecraftheads.api.HeadCollectionImpl;
import org.everbuild.minecraftheads.api.HeadImpl;
import org.everbuild.minecraftheads.api.HeadTag;
import org.everbuild.minecraftheads.api.HeadTagImpl;
import org.everbuild.minecraftheads.api.InitResult;
import org.everbuild.minecraftheads.models.CategoriesModel;
import org.everbuild.minecraftheads.models.CategoryModel;
import org.everbuild.minecraftheads.models.CollectionsModel;
import org.everbuild.minecraftheads.models.HeadModel;
import org.everbuild.minecraftheads.models.HeadsModel;
import org.everbuild.minecraftheads.models.PaginationModel;
import org.everbuild.minecraftheads.models.TagModel;
import org.everbuild.minecraftheads.models.TagsModel;
import org.everbuild.minecraftheads.request.BoundRequestFactory;
import org.everbuild.minecraftheads.request.RequestConfiguration;
import org.everbuild.minecraftheads.request.UnboundRequestFactory;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class MinecraftHeadsImpl implements MinecraftHeads {
    private final BoundRequestFactory requestFactory;
    private final boolean isDemo;
    private final String baseUrl;
    private final boolean useTags;
    private InitResult initResult = new InitResultImpl(false, List.of(), null);
    private final ArrayList<HeadCategory> categories = new ArrayList<>();
    private final ArrayList<HeadTag> tags = new ArrayList<>();
    private final ArrayList<Head> heads = new ArrayList<>();

    public MinecraftHeadsImpl(String apiKey, String appId, boolean isDemo, UnboundRequestFactory requestFactory, String baseUrl, boolean useTags) {
        this.isDemo = isDemo;
        this.baseUrl = baseUrl;
        this.useTags = useTags;
        RequestConfiguration config = new RequestConfiguration(appId, apiKey);
        this.requestFactory = new BoundRequestFactory(requestFactory, config);
    }

    private Map<String, String> withDemo(Map<String, String> params) {
        if (isDemo) {
            HashMap<String, String> newParams = new HashMap<>(params);
            newParams.put("demo", "true");
            return newParams;
        }
        return params;
    }

    private CompletableFuture<HeadsModel> createHeadRequest(int page) {
        return requestFactory.httpGet(
                baseUrl + "/heads/custom-heads",
                withDemo(Map.of(
                        "uuid", "true",
                        "id", "true",
                        "tags", String.valueOf(useTags),
                        "page", String.valueOf(page)
                )),
                HeadsModel.CODEC
        );
    }

    public CompletableFuture<Void> init() {
        CompletableFuture<Void> categoriesFuture = requestFactory.httpGet(
                        baseUrl + "/heads/categories",
                        withDemo(Map.of()),
                        CategoriesModel.CODEC
                )
                .thenAccept(categoriesModel -> {
                    for (CategoryModel category : categoriesModel.categories()) {
                        categories.add(new HeadCategoryImpl(category.id(), category.name()));
                    }
                });

        CompletableFuture<Void> tagsFuture = null;
        if (useTags) {
            tagsFuture = requestFactory.httpGet(
                            baseUrl + "/heads/tags",
                            withDemo(Map.of()),
                            TagsModel.CODEC
                    )
                    .thenAccept(tagsModel -> {
                        for (TagModel tag : tagsModel.tags()) {
                            tags.add(new HeadTagImpl(tag.id(), tag.name()));
                        }
                    });
        }

        return CompletableFuture.allOf(
                        Stream.of(categoriesFuture, tagsFuture)
                                .filter(Objects::nonNull)
                                .toArray(CompletableFuture[]::new)
                )
                .thenCompose(_void -> createHeadRequest(1))
                .thenCompose(headsModel -> {
                    initResult = new InitResultImpl(true, headsModel.warnings(), headsModel.meta());
                    PaginationModel pagination = headsModel.pagination();
                    loadHeads(headsModel.heads());
                    if (pagination == null || pagination.currentPage() == pagination.lastPage()) {
                        return CompletableFuture.completedFuture(null);
                    }

                    return CompletableFuture.allOf(
                            IntStream.rangeClosed(2, pagination.lastPage())
                                    .mapToObj(this::createHeadRequest)
                                    .map(fut ->
                                            fut.thenAccept(model ->
                                                    loadHeads(model.heads())
                                            )
                                    )
                                    .toArray(CompletableFuture[]::new)
                    );
                });
    }

    private synchronized void loadHeads(List<HeadModel> headListFromModel) {
        for (HeadModel head : headListFromModel) {
            HeadCategory headCategory = categories.stream()
                    .filter(c -> c.getId() == head.categoryId())
                    .findFirst()
                    .orElse(null);

            List<HeadTag> headTags = tags.stream()
                    .filter(c -> head.tagIds().contains(c.getId()))
                    .toList();

            HeadImpl headImpl = new HeadImpl(head.id(), head.name(), headCategory, head.uuid(), headTags, head.url());
            heads.add(headImpl);
            if (headCategory != null) {
                headCategory.addHead(headImpl);
            }
            headTags.forEach(tag -> tag.addHead(headImpl));
        }
    }

    private CompletableFuture<List<HeadCollection>> getHeadCollections(String endpoint, Map<String, String> extraParams) {
        return requestFactory.httpGet(endpoint, withDemo(extraParams), CollectionsModel.CODEC)
                .thenApply(model -> model.collections()
                        .stream()
                        .map(elem -> (HeadCollection) new HeadCollectionImpl(
                                elem.name(),
                                elem.heads()
                                        .stream()
                                        .map(this::getHead)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .toList()
                        ))
                        .toList()
                );
    }

    @Override
    public List<HeadCategory> getCategories() {
        return categories;
    }

    @Override
    public List<HeadTag> getTags() {
        return tags;
    }

    @Override
    public List<Head> getCustomHeads() {
        return heads;
    }

    @Override
    public Optional<Head> getHead(int id) {
        return heads.stream().filter(head -> head.getId() == id).findFirst();
    }

    @Override
    public Optional<HeadCategory> getCategory(int id) {
        return categories.stream().filter(category -> category.getId() == id).findFirst();
    }

    @Override
    public Optional<HeadTag> getTag(int id) {
        return tags.stream().filter(tag -> tag.getId() == id).findFirst();
    }

    @Override
    public InitResult getInitResult() {
        return initResult;
    }

    @Override
    public CompletableFuture<List<HeadCollection>> getHeadCollectionsByLicenseOwner() {
        return getHeadCollections(baseUrl + "/heads/collections", Map.of());
    }

    @Override
    public CompletableFuture<List<HeadCollection>> getHeadCollectionsByPlayer(UUID playerUUID) {
        return getHeadCollections(baseUrl + "/heads/collections/player", Map.of("player_uuid", playerUUID.toString()));
    }

    @Override
    public CompletableFuture<List<HeadCollection>> getHeadCollectionsByPlayer(Player player) {
        return getHeadCollectionsByPlayer(player.getUuid());
    }
}
