package archives.tater.relreg.impl.client;

import net.minecraft.core.LayeredRegistryAccess;

import java.util.List;

public enum ReloadableClientRegistryLayer {
    BUILTIN,
    RELOADABLE;

    private static final List<ReloadableClientRegistryLayer> VALUES = List.of(values());

    public static LayeredRegistryAccess<ReloadableClientRegistryLayer> createRegistryAccess() {
        return new LayeredRegistryAccess<>(VALUES);
    }
}
