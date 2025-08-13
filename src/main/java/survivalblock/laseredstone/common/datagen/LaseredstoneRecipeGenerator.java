package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

import java.util.concurrent.CompletableFuture;

public class LaseredstoneRecipeGenerator extends FabricRecipeProvider {

    public LaseredstoneRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                this.createShaped(RecipeCategory.REDSTONE, LaseredstoneItems.RECEIVER)
                        .pattern("|#|")
                        .pattern("#o#")
                        .pattern("|#|")
                        .input('o', Blocks.OBSERVER)
                        .input('|', Items.NETHERITE_INGOT)
                        .input('#', Items.QUARTZ)
                        .criterion(hasItem(Blocks.OBSERVER), conditionsFromItem(Blocks.OBSERVER))
                        .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                        .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                        .offerTo(this.exporter);
                this.createShaped(RecipeCategory.REDSTONE, LaseredstoneItems.HORIZONTAL_VERTICAL_MIRROR)
                        .pattern("  #")
                        .pattern(" g#")
                        .pattern("###")
                        .input('g', Blocks.GLASS)
                        .input('#', Items.QUARTZ)
                        .criterion(hasItem(Blocks.GLASS), conditionsFromItem(Blocks.GLASS))
                        .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                        .offerTo(this.exporter);
                this.createShaped(RecipeCategory.REDSTONE, LaseredstoneItems.HORIZONTAL_HORIZONTAL_MIRROR)
                        .pattern("#g#")
                        .pattern("###")
                        .input('g', Blocks.GLASS)
                        .input('#', Items.QUARTZ)
                        .criterion(hasItem(Blocks.GLASS), conditionsFromItem(Blocks.GLASS))
                        .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                        .offerTo(this.exporter);
                this.createShaped(RecipeCategory.REDSTONE, LaseredstoneItems.LASER)
                        .pattern("|#|")
                        .pattern("#E#")
                        .pattern("|#|")
                        .input('E', Blocks.BEACON)
                        .input('|', Blocks.IRON_BLOCK)
                        .input('#', ConventionalItemTags.GEMS)
                        .criterion(hasItem(Blocks.BEACON), conditionsFromItem(Blocks.BEACON))
                        .criterion(hasItem(Blocks.IRON_BLOCK), conditionsFromItem(Blocks.IRON_BLOCK))
                        .criterion("has_gem", conditionsFromTag(ConventionalItemTags.GEMS))
                        .offerTo(this.exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
