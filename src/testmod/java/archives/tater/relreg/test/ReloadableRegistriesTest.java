package archives.tater.relreg.test;

import archives.tater.relreg.api.ReloadableRegistries;

import net.fabricmc.api.ModInitializer;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;

public class ReloadableRegistriesTest implements ModInitializer {
    public static final ResourceKey<Registry<String>> TEST = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("relregtest", "test"));
    public static final Codec<String> TEST_CODEC = Codec.STRING.fieldOf("x").codec();

    @Override
    public void onInitialize() {
        ReloadableRegistries.registerSynced(TEST, TEST_CODEC);
    }
}