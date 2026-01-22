package archives.tater.relreg.mixin.client;

import archives.tater.relreg.api.client.HasClientReloadableRegistries;
import archives.tater.relreg.impl.client.MutableReloadableRegistries;
import archives.tater.relreg.impl.client.ReloadableClientRegistryLayer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin implements HasClientReloadableRegistries, MutableReloadableRegistries {
    @Shadow
    @Final
    private RegistryAccess.Frozen registryAccess;

    @Unique
    private LayeredRegistryAccess<ReloadableClientRegistryLayer> layeredRegistryAccess = ReloadableClientRegistryLayer.createRegistryAccess();

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initLayeredAccess(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        layeredRegistryAccess = layeredRegistryAccess.replaceFrom(ReloadableClientRegistryLayer.BUILTIN, registryAccess);
    }

    @Override
    public RegistryAccess relreg_reloadableRegistries() {
        return layeredRegistryAccess.compositeAccess();
    }

    @Override
    public void relreg_setReloadableRegistries(RegistryAccess.Frozen reloadableRegistries) {
        layeredRegistryAccess = layeredRegistryAccess.replaceFrom(ReloadableClientRegistryLayer.RELOADABLE, reloadableRegistries);
    }
}
