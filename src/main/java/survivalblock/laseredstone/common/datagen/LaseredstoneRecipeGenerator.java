package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

import java.util.concurrent.CompletableFuture;

public class LaseredstoneRecipeGenerator extends FabricRecipeProvider {

    public LaseredstoneRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                this.shaped(RecipeCategory.REDSTONE, LaseredstoneItems.RECEIVER)
                        .pattern("|#|")
                        .pattern("#o#")
                        .pattern("|#|")
                        .define('o', Blocks.OBSERVER)
                        .define('|', Items.NETHERITE_INGOT)
                        .define('#', Items.QUARTZ)
                        .unlockedBy(getHasName(Blocks.OBSERVER), has(Blocks.OBSERVER))
                        .unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .save(this.output);
                this.shaped(RecipeCategory.REDSTONE, LaseredstoneItems.HORIZONTAL_VERTICAL_MIRROR)
                        .pattern("  #")
                        .pattern(" g#")
                        .pattern("###")
                        .define('g', Blocks.GLASS)
                        .define('#', Items.QUARTZ)
                        .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .save(this.output);
                this.shaped(RecipeCategory.REDSTONE, LaseredstoneItems.HORIZONTAL_HORIZONTAL_MIRROR)
                        .pattern("#g#")
                        .pattern("###")
                        .define('g', Blocks.GLASS)
                        .define('#', Items.QUARTZ)
                        .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                        .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                        .save(this.output);
                this.shaped(RecipeCategory.REDSTONE, LaseredstoneItems.LASER)
                        .pattern("|#|")
                        .pattern("#E#")
                        .pattern("|#|")
                        .define('E', LaseredstoneTags.LASER_POWERER)
                        .define('|', Blocks.IRON_BLOCK)
                        .define('#', ConventionalItemTags.GEMS)
                        .unlockedBy("has_laser_powerer", has(LaseredstoneTags.LASER_POWERER))
                        .unlockedBy(getHasName(Blocks.IRON_BLOCK), has(Blocks.IRON_BLOCK))
                        .unlockedBy("has_gem", has(ConventionalItemTags.GEMS))
                        .save(this.output);
                this.shaped(RecipeCategory.REDSTONE, LaseredstoneItems.LENS)
                        .pattern("|#|")
                        .pattern("|G|")
                        .pattern("|#|")
                        .define('G', ConventionalItemTags.GEMS)
                        .define('|', Items.NETHERITE_INGOT)
                        .define('#', Blocks.GLASS)
                        .unlockedBy("has_gem", has(ConventionalItemTags.GEMS))
                        .unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                        .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                        .save(this.output);
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
