package archives.tater.relreg.impl.sync;

import archives.tater.relreg.mixin.RegistrySynchronizationAccessor;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReloadableRegistrySync {
    public static final List<RegistryDataLoader.RegistryData<?>> SYNCED_RELOADABLE_REGISTRIES = new ArrayList<>();

    public static <T> void registerSynced(ResourceKey<Registry<T>> key, Codec<T> syncCodec) {
        SYNCED_RELOADABLE_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(key, syncCodec, false));
    }

    public static SyncReloadableRegistriesPayload getSyncPayload(RegistryAccess registries) {
        var syncEntries = new ArrayList<ClientboundRegistryDataPacket>();
        for (var registry : SYNCED_RELOADABLE_REGISTRIES)
            RegistrySynchronizationAccessor.invokePackRegistry(registries.createSerializationContext(NbtOps.INSTANCE), registry, registries, Set.of(), (key, entries) -> {
                syncEntries.add(new ClientboundRegistryDataPacket(key, entries));
            });
        return new SyncReloadableRegistriesPayload(syncEntries);
    }
}
