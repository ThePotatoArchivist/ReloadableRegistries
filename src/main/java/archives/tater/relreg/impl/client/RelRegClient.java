package archives.tater.relreg.impl.client;

import archives.tater.relreg.impl.RelReg;
import archives.tater.relreg.impl.sync.ReloadableRegistrySync;
import archives.tater.relreg.impl.sync.SyncReloadableRegistriesPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceProvider;

import dev.xpple.clientarguments.arguments.CResourceLocationArgument;

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
            Map<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> registryMap =
                    payload.registries().stream().collect(Collectors.toMap(
                            ClientboundRegistryDataPacket::registry,
                            ClientboundRegistryDataPacket::entries
                    ));

            var connection = context.player().connection;
            ((MutableReloadableRegistries) connection).relreg_setReloadableRegistries(
                    RegistryDataLoader.load(registryMap, ResourceProvider.EMPTY, connection.registryAccess(), ReloadableRegistrySync.SYNCED_RELOADABLE_REGISTRIES)
            );
        });

        if (FabricLoader.getInstance().isDevelopmentEnvironment() && FabricLoader.getInstance().isModLoaded("clientarguments"))
            registerCommands();
    }

    private static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(literal("relreg_client")
                        .then(argument("registry", CResourceLocationArgument.id()).executes(command -> RelReg.executeListRegistry(
                                requireNonNull(command.getSource().getClient().getConnection()).relreg_reloadableRegistries(),
                                CResourceLocationArgument.getId(command, "registry"),
                                command.getSource()::sendFeedback
                        )))
                );
        });
    }
}
