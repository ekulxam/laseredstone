package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

import java.util.concurrent.CompletableFuture;

public class LaseredstoneEnUsLangGenerator extends FabricLanguageProvider {

    protected LaseredstoneEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, "Horizontal-Vertical Mirror");
        translationBuilder.add(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, "Horizontal-Horizontal Mirror");
        translationBuilder.add(LaseredstoneBlocks.LASER, "Laser");
    }
}
