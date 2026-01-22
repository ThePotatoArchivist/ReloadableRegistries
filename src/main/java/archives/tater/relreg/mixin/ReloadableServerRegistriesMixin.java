package archives.tater.relreg.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin {
    @WrapOperation(
            method = "method_58279",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/registries/Registries;elementsDirPath(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/String;")
    )
    private static String fixPath(ResourceKey<? extends Registry<?>> registryKey, Operation<String> original) {
        var id = registryKey.location();
        if (id.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE))
            return original.call(registryKey);
        return id.getNamespace() + "/" + id.getPath();
    }
}
