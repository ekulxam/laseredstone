package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import survivalblock.laseredstone.common.Laseredstone;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LaseredstoneDynamicRegistryGenerator extends FabricDynamicRegistryProvider {

    public LaseredstoneDynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        List<ResourceKey<? extends Registry<?>>> registryKeys = List.of(Registries.DAMAGE_TYPE);
        registryKeys.forEach(key -> {
            registries.lookupOrThrow(key)
                    .listElements()
                    .filter(ref ->
                            Laseredstone.MOD_ID.equals(ref.key()./*? if <1.21.11 {*//*identifier()*//*?} *//*? else {*/ location() /*?}*/.getNamespace()))
                    .forEachOrdered(ref ->
                            entries.add(ref.key(), ref.value()));
        });
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
