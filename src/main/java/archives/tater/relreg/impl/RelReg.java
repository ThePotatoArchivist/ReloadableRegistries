package archives.tater.relreg.impl;

import archives.tater.relreg.impl.sync.ReloadableRegistrySync;
import archives.tater.relreg.impl.sync.SyncReloadableRegistriesPayload;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RelReg implements ModInitializer {

    public static final String MOD_ID = "relreg";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int executeListRegistry(HolderLookup.Provider registryAccess, Identifier registryId, Consumer<Component> responder) {
        var entries = registryAccess.lookupOrThrow(ResourceKey.createRegistryKey(registryId)).listElements().toList();
        responder.accept(Component.literal("Registry " + registryId + " has " + entries.size() + " registries:"));
        for (var entry : entries) {
            responder.accept(Component.literal(entry.key().identifier().toString()).withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(entry.value().toString()).withStyle(ChatFormatting.GRAY)));
        }
        return entries.size();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("relreg_server")
                    .then(argument("registry", IdentifierArgument.id()).executes(command -> executeListRegistry(
                            command.getSource().getServer().relreg_reloadableRegistries(),
                            IdentifierArgument.getId(command, "registry"),
                            text -> command.getSource().sendSuccess(() -> text, false)
                    )))
            );
        });
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        PayloadTypeRegistry.playS2C().registerLarge(SyncReloadableRegistriesPayload.TYPE, SyncReloadableRegistriesPayload.CODEC, 128 * 1024 * 1024);

        ServerPlayerEvents.JOIN.register(serverPlayer ->
                ServerPlayNetworking.send(serverPlayer, ReloadableRegistrySync.getSyncPayload(
                        serverPlayer.level().getServer().relreg_reloadableRegistries()
                ))
        );

        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            registerCommands();
    }
}
