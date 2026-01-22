package archives.tater.relreg.mixin.client;

import archives.tater.relreg.api.client.HasClientReloadableRegistries;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements HasClientReloadableRegistries {
    @Shadow
    @Final
    private ClientPacketListener connection;

    @Override
    public RegistryAccess relreg_reloadableRegistries() {
        return connection.relreg_reloadableRegistries();
    }
}
