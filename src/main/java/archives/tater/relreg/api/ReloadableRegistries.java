package archives.tater.relreg.api;

import archives.tater.relreg.impl.ReloadableRegistriesImpl;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootDataType;

import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Allows registering reloadable registries that fill from datapack files and unlike dynamic registries can be updated
 * with the {@code /reload} command.
 * <p>
 * Reloadable registries do not appear the registry access returned by {@link Level#registryAccess()}, they can only
 * be accessed through {@link MinecraftServer#reloadableRegistries()} or
 * {@link ClientPacketListener#relreg_reloadableRegistries()}/{@link ClientLevel#relreg_reloadableRegistries()} (if
 * synced). Otherwise, they act like normal registries.
 * <p>
 * Registration is similar to {@link DynamicRegistries}.
 */
@SuppressWarnings("unused")
public final class ReloadableRegistries {
    private ReloadableRegistries() {}

    /**
     * Register a reloadable registry with a validator
     */
    public static <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, LootDataType.@Nullable Validator<T> validator) {
        ReloadableRegistriesImpl.register(key, codec, validator);
    }

    /**
     * Register a reloadable registry
     */
    public static <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec) {
        register(key, codec, null);
    }

    /**
     * Register a synced reloadable registry with a validator and a different codec for syncing
     */
    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, LootDataType.@Nullable Validator<T> validator) {
        ReloadableRegistriesImpl.registerSynced(key, codec, syncCodec, validator);
    }

    /**
     * Register a synced reloadable registry with a validator
     */
    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, LootDataType.@Nullable Validator<T> validator) {
        registerSynced(key, codec, codec, validator);
    }

    /**
     * Register a synced reloadable registry with a different codec for syncing
     */
    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec) {
        registerSynced(key, codec, syncCodec, null);
    }

    /**
     * Register a synced reloadable registry
     */
    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec) {
        registerSynced(key, codec, codec);
    }

    /**
     * Get all reloadable registries including vanilla ones (see {@link LootDataType})
     */
    public static @UnmodifiableView List<ResourceKey<? extends Registry<?>>> getReloadableRegistries() {
        return ReloadableRegistriesImpl.getReloadableRegistries();
    }

    /**
     * Get all reloadable registries excluding vanilla ones
     */
    public static @UnmodifiableView List<ResourceKey<? extends Registry<?>>> getCustomReloadableRegistries() {
        return ReloadableRegistriesImpl.getCustomReloadableRegistries();
    }
}
