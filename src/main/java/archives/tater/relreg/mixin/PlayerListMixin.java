package archives.tater.relreg.mixin;

import archives.tater.relreg.impl.ReloadableRegistriesImpl;
import archives.tater.relreg.impl.SyncReloadableRegistryPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(
            method = "reloadResources",
            at = @At("HEAD")
    )
    private void createPayload(CallbackInfo ci, @Share("payload")LocalRef<SyncReloadableRegistryPayload> payload) {
        payload.set(ReloadableRegistriesImpl.getSyncPayload((RegistryAccess) server.reloadableRegistries().lookup()));
    }

    @WrapOperation(
            method = "reloadResources",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V")
    )
    private void sendPayload(ServerGamePacketListenerImpl instance, Packet<?> packet, Operation<Void> original, @Share("payload")LocalRef<SyncReloadableRegistryPayload> payload) {
        original.call(instance, packet);
        ServerPlayNetworking.send(instance.player, payload.get());
    }
}
