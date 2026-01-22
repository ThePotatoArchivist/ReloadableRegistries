package archives.tater.relreg.impl.client;

import net.minecraft.core.RegistryAccess;

public interface MutableReloadableRegistries {
    void relreg_setReloadableRegistries(RegistryAccess.Frozen reloadableRegistries);
}
