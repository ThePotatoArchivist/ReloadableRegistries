package archives.tater.relreg.test;

import archives.tater.relreg.api.ReloadableRegistries;

import net.fabricmc.api.ModInitializer;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ReloadableRegistriesTest implements ModInitializer {
    public static final ResourceKey<Registry<String>> TEST = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("relregtest", "test"));

    @Override
    public void onInitialize() {
        ReloadableRegistries.registerSynced(TEST, Codec.STRING.fieldOf("x").codec());
    }
}