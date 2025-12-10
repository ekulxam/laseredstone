package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
//? if >=1.21.11 {
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.rule.GameRule;
//?}
//? if <1.21.11 {
/*import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
*///?}
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneGameRules {

    public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(Laseredstone.id("gamerule_category"), Text.translatable("gamerule.category.laseredstone").formatted(Formatting.YELLOW, Formatting.BOLD));
    public static final /*? <1.21.11 {*/ /*GameRules.Key<GameRules.BooleanRule> *//*?} else {*/ GameRule<Boolean> /*?}*/  MULTI_DAMAGE = registerBool("take_damage_from_multiple_lasers", true);

    public static /*? <1.21.11 {*/ /*GameRules.Key<GameRules.BooleanRule> *//*?} else {*/ GameRule<Boolean> /*?}*/ registerBool(String name, boolean defaultValue) {
        return register(name, /*? <1.21.11 {*/ /*GameRuleFactory.createBooleanRule(defaultValue) *//*?} else {*/ GameRuleBuilder.forBoolean(defaultValue) /*?}*/);
    }

    public static <T /*? <1.21.11 {*/ /*extends GameRules.Rule<T> *//*?}*/> /*? <1.21.11 {*/ /*GameRules.Key<T> *//*?} else {*/ GameRule<T> /*?}*/ register(String name, /*? <1.21.11 {*/ /*GameRules.Type<T> type *//*?} else {*/ GameRuleBuilder<T> builder /*?}*/) {
        //? <1.21.11
        /*return GameRuleRegistry.register(Laseredstone.MOD_ID + ":" + name, CATEGORY, type);*/
        //? >=1.21.11
        return builder.category(CATEGORY).buildAndRegister(Laseredstone.id(name));
    }

    public static void init() {

    }
}
