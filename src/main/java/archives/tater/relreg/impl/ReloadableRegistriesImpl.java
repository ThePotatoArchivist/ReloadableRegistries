package archives.tater.relreg.impl;

import archives.tater.relreg.impl.sync.ReloadableRegistrySync;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootDataType;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNullElse;

public class ReloadableRegistriesImpl {

	public static final List<ResourceKey<? extends Registry<?>>> RELOADABLE_REGISTRIES =
			Lists.newArrayList(LootDataType.PREDICATE.registryKey(), LootDataType.MODIFIER.registryKey(), LootDataType.TABLE.registryKey());

	public static final List<ResourceKey<? extends Registry<?>>> CUSTOM_RELOADABLE_REGISTRIES = new ArrayList<>();

	public static final List<LootDataType<?>> CUSTOM_LOOT_DATA_TYPES = new ArrayList<>();

	private static final LootDataType.Validator<?> EMPTY_VALIDATOR = (validationContext, resourceKey, object) -> {};

	@SuppressWarnings("unchecked")
    private static <T> LootDataType.Validator<T> emptyValidator() {
		return (LootDataType.Validator<T>) EMPTY_VALIDATOR;
	}

    public static <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, LootDataType.@Nullable Validator<T> validator) {
		RELOADABLE_REGISTRIES.add(key);
		CUSTOM_RELOADABLE_REGISTRIES.add(key);
		CUSTOM_LOOT_DATA_TYPES.add(new LootDataType<>(key, codec, requireNonNullElse(validator, emptyValidator())));
	}

	public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, LootDataType.@Nullable Validator<T> validator) {
		register(key, codec, validator);
		ReloadableRegistrySync.registerSynced(key, syncCodec);
	}

	public static @UnmodifiableView List<ResourceKey<? extends Registry<?>>> getReloadableRegistries() {
		return unmodifiableList(RELOADABLE_REGISTRIES);
    }

    public static @UnmodifiableView List<ResourceKey<? extends Registry<?>>> getCustomReloadableRegistries() {
        return unmodifiableList(CUSTOM_RELOADABLE_REGISTRIES);
    }

	public static Stream<LootDataType<?>> streamLootDataTypes() {
		return CUSTOM_LOOT_DATA_TYPES.stream();
	}

}