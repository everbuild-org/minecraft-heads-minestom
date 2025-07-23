package org.everbuild.minecraftheads;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.ping.Status;
import org.everbuild.minecraftheads.api.Head;
import org.everbuild.minecraftheads.api.InitResult;

import java.io.IOException;
import java.io.InputStream;

public class DemoServer {
    private final MinecraftServer server = MinecraftServer.init();

    public DemoServer(String apiKey) {
        MinecraftHeads minecraftHeads = MinecraftHeads.builder(apiKey)
                .demo(true)
                .tags(true)
                .build()
                .join(); // You don't want to do this, wait async here

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        Instance instance = instanceManager.createInstanceContainer();
        instance.setGenerator(generationUnit -> generationUnit.modifier().fillHeight(0, 65, Block.WHITE_STAINED_GLASS));

        EventNode<Event> events = MinecraftServer.getGlobalEventHandler();
        events
                .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                    event.setSpawningInstance(instance);
                    event.getPlayer().setRespawnPoint(new Pos(0.0, 65.0, 0.0));
                })
                .addListener(ServerListPingEvent.class, event ->
                        event.setStatus(Status.builder()
                                .favicon(readFaviconPng())
                                .description(Component.text("A Minestom server running the Minecraft-Heads API demo"))
                                .build())
                )
                .addListener(PlayerSpawnEvent.class, event -> {
                    event.getPlayer().sendMessage("Hello, this is a demo server running the Minecraft-Heads API! Type a Head ID in chat to obtain it");
                    event.getPlayer().sendMessage("Loaded heads: " + minecraftHeads.getCustomHeads().size());
                    event.getPlayer().sendMessage("Loaded categories: " + minecraftHeads.getCategories().size());
                    event.getPlayer().sendMessage("Loaded tags: " + minecraftHeads.getTags().size());

                    InitResult initResult = minecraftHeads.getInitResult();
                    if (!initResult.isSuccess()) {
                        event.getPlayer().sendMessage(Component.text("Initialization failed: " + initResult.getWarnings().stream().reduce("", (a, b) -> a + "\n" + b)));
                        return;
                    } else if (!initResult.getWarnings().isEmpty()) {
                        event.getPlayer().sendMessage(Component.text("Initialization successful, but warnings were found: " + initResult.getWarnings().stream().reduce("", (a, b) -> a + "\n" + b)));
                        return;
                    }

                    Head example = minecraftHeads.getCustomHeads().getFirst();
                    event.getPlayer().getInventory().addItemStack(
                            ItemStack.of(Material.PLAYER_HEAD)
                                    .with(DataComponents.PROFILE, example.getHeadProfile())
                    );
                })
                .addListener(PlayerChatEvent.class, event -> {
                    Head example = minecraftHeads.getCustomHeads().stream().filter(head -> String.valueOf(head.getId()).equals(event.getRawMessage())).findFirst().orElse(null);
                    if (example == null) {
                        event.getPlayer().sendMessage(Component.text("No head found with id " + event.getRawMessage()));
                        return;
                    }
                    event.getPlayer().getInventory().addItemStack(
                            ItemStack.of(Material.PLAYER_HEAD)
                                    .with(DataComponents.PROFILE, example.getHeadProfile())
                    );
                });
    }

    public void start() {
        server.start("0.0.0.0", 25565);
    }

    private byte[] readFaviconPng() {
        try (InputStream inputStream = DemoServer.class.getResourceAsStream("/chest.png")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + "/chest.png");
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read favicon", e);
        }
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("MCHEADS_API_KEY");
        if (apiKey == null) {
            System.err.println("MCHEADS_API_KEY environment variable not set. Go to https://minecraft-heads.com/settings/api to get an API key.");
            System.exit(1);
        }
        new DemoServer(apiKey).start();
    }
}
