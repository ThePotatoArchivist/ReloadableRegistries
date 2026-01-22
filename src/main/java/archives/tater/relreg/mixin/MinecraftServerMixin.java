package archives.tater.relreg.mixin;

import archives.tater.relreg.api.HasReloadableRegistries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements HasReloadableRegistries {
    @Shadow
    public abstract ReloadableServerRegistries.Holder reloadableRegistries();

    @Override
    public RegistryAccess relreg_reloadableRegistries() {
        return reloadableRegistries().get();
    }
}
