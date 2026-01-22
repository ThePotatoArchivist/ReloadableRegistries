package archives.tater.relreg.api;

import archives.tater.relreg.impl.ReloadableRegistriesImpl;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootDataType;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public final class ReloadableRegistries {
    private ReloadableRegistries() {}

    public static <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, LootDataType.@Nullable Validator<T> validator) {
        ReloadableRegistriesImpl.register(key, codec, validator);
    }

    public static <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec) {
        register(key, codec, null);
    }

    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, LootDataType.@Nullable Validator<T> validator) {
        ReloadableRegistriesImpl.registerSynced(key, codec, syncCodec, validator);
    }

    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, LootDataType.@Nullable Validator<T> validator) {
        registerSynced(key, codec, codec, validator);
    }

    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec) {
        registerSynced(key, codec, syncCodec, null);
    }

    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec) {
        registerSynced(key, codec, codec);
    }

    public static @Unmodifiable List<ResourceKey<? extends Registry<?>>> getReloadableRegistries() {
        return ReloadableRegistriesImpl.getReloadableRegistries();
    }

    public static @Unmodifiable List<ResourceKey<? extends Registry<?>>> getCustomReloadableRegistries() {
        return ReloadableRegistriesImpl.getCustomReloadableRegistries();
    }
}
