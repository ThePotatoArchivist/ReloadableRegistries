package archives.tater.relreg.impl.sync;

import archives.tater.relreg.impl.RelReg;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;

import java.util.List;

public record SyncReloadableRegistriesPayload(
        List<ClientboundRegistryDataPacket> registries
) implements CustomPacketPayload {

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Type<SyncReloadableRegistriesPayload> TYPE = new Type<>(RelReg.id("sync_reloadable_registries"));
    public static final StreamCodec<FriendlyByteBuf, SyncReloadableRegistriesPayload> CODEC =
            ClientboundRegistryDataPacket.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    SyncReloadableRegistriesPayload::new,
                    SyncReloadableRegistriesPayload::registries
            );
}
