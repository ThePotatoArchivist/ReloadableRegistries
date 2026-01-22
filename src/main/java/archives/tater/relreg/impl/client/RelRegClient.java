package archives.tater.relreg.impl.client;

import archives.tater.relreg.impl.RelReg;
import archives.tater.relreg.impl.sync.SyncReloadableRegistriesPayload;
import archives.tater.relreg.impl.sync.ReloadableRegistrySync;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.tags.TagNetworkSerialization;

import dev.xpple.clientarguments.arguments.CIdentifierArgument;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RelRegClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncReloadableRegistriesPayload.TYPE, (payload, context) -> {
            Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> registryMap =
                    payload.registries().stream().collect(Collectors.toMap(
                            ClientboundRegistryDataPacket::registry,
                            packet -> new RegistryDataLoader.NetworkedRegistryData(packet.entries(), TagNetworkSerialization.NetworkPayload.EMPTY)
                    ));

            ((MutableReloadableRegistries) context.player().connection).relreg_setReloadableRegistries(
                    RegistryDataLoader.load(registryMap, ResourceProvider.EMPTY, List.of(), ReloadableRegistrySync.SYNCED_RELOADABLE_REGISTRIES)
            );
        });

        if (FabricLoader.getInstance().isDevelopmentEnvironment() && FabricLoader.getInstance().isModLoaded("clientarguments"))
            registerCommands();
    }

    private static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(literal("relreg_client")
                        .then(argument("registry", CIdentifierArgument.id()).executes(command -> RelReg.executeListRegistry(
                                requireNonNull(command.getSource().getClient().getConnection()).relreg_reloadableRegistries(),
                                CIdentifierArgument.getId(command, "registry"),
                                command.getSource()::sendFeedback
                        )))
                );
        });
    }
}
