package archives.tater.relreg.impl;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;

import java.util.List;

public record SyncReloadableRegistryPayload(
        List<ClientboundRegistryDataPacket> entries
) implements CustomPacketPayload {

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Type<SyncReloadableRegistryPayload> TYPE = new Type<>(ReloadableRegistriesImpl.id("sync_reloadable_registries"));
    public static final StreamCodec<FriendlyByteBuf, SyncReloadableRegistryPayload> CODEC =
            ClientboundRegistryDataPacket.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    SyncReloadableRegistryPayload::new,
                    SyncReloadableRegistryPayload::entries
            );
}
