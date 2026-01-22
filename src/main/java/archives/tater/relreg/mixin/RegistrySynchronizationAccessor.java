package archives.tater.relreg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.repository.KnownPack;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(RegistrySynchronization.class)
public interface RegistrySynchronizationAccessor {
    @Invoker
    static <T> void invokePackRegistry(
            DynamicOps<Tag> ops,
            RegistryDataLoader.RegistryData<T> registryData,
            RegistryAccess registryAccess,
            Set<KnownPack> packs,
            BiConsumer<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> packetSender
    ) {
        throw new AssertionError();
    }
}
