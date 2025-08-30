package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneGameRules;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

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
        translationBuilder.add(LaseredstoneBlocks.RECEIVER, "Receiver");
        translationBuilder.add(LaseredstoneBlocks.LENS, "Lens");
        translationBuilder.add(LaseredstoneTags.ALWAYS_ALLOWS_LASERS, "Laseredstone - Always Allows Lasers");
        translationBuilder.add(LaseredstoneTags.ALWAYS_DENIES_LASERS, "Laseredstone - Always Denies Lasers");
        translationBuilder.add(LaseredstoneTags.BYPASSES_CREATIVE_MODE, "Laseredstone - Bypasses Creative Mode");
        translationBuilder.add(LaseredstoneTags.LASER_PROOF, "Laseredstone - Laser-Proof");
        translationBuilder.add("death.attack.laseredstone.laser", "%1$s was fried by an overcharged laser");
        translationBuilder.add("death.attack.laseredstone.laser.player", "%1$s was fried by an overcharged laser whilst fighting %2$s");
        translationBuilder.add("death.attack.laseredstone.laser.item", "%1$s was fried by an overcharged laser whilst fighting %2$s using %3$s");
        translationBuilder.add(LaseredstoneGameRules.MULTI_DAMAGE.getTranslationKey(), "Allow Taking Damage From Multiple Lasers Simultaneously");
        translationBuilder.add("gamerule.category.laseredstone", "Laseredstone");
    }
}
