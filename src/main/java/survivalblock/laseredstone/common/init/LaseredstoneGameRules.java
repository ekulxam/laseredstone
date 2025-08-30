package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneGameRules {

    public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(Laseredstone.id("gamerule_category"), Text.translatable("gamerule.category.laseredstone").formatted(Formatting.YELLOW, Formatting.BOLD));
    public static final GameRules.Key<GameRules.BooleanRule> MULTI_DAMAGE = registerBool("laseredstone:takeDamageFromMultipleLasers", true);

    public static GameRules.Key<GameRules.BooleanRule> registerBool(String name, boolean defaultValue) {
        return register(name, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, CATEGORY, type);
    }

    public static void init() {

    }
}
