package archives.tater.relreg.api.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;

/**
 * Extension to {@link ClientLevel} and {@link ClientPacketListener} allowing access to reloadable registries
 */
public interface HasClientReloadableRegistries {
    default RegistryAccess relreg_reloadableRegistries() {
        throw new AssertionError("Implemented by mixin");
    }
}
