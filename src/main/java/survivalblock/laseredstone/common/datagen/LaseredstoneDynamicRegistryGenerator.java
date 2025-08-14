package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.laseredstone.common.Laseredstone;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LaseredstoneDynamicRegistryGenerator extends FabricDynamicRegistryProvider {

    public LaseredstoneDynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        List<RegistryKey<? extends Registry<?>>> registryKeys = List.of(RegistryKeys.DAMAGE_TYPE);
        registryKeys.forEach(key -> {
            registries.getOrThrow(key)
                    .streamEntries()
                    .filter(ref ->
                            Laseredstone.MOD_ID.equals(ref.registryKey().getValue().getNamespace()))
                    .forEachOrdered(ref ->
                            entries.add(ref.registryKey(), ref.value()));
        });
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
