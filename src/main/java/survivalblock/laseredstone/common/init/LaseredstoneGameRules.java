package survivalblock.laseredstone.common.init;
//? if <26
//import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
//? if >=1.21.11 {
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
//?}
//? if <1.21.11 {
/*import net.minecraft.world.level.GameRules;
*///?}
//? if <26 {
//?} else {
import net.minecraft.world.level.gamerules.GameRuleCategory;
//?}
import survivalblock.atmosphere.registrar.GameRuleRegistrant;
import survivalblock.laseredstone.common.Laseredstone;

public final class LaseredstoneGameRules {
    private LaseredstoneGameRules() {
    }

    private static final GameRuleRegistrant REGISTRANT = new GameRuleRegistrant(Laseredstone.MOD_ID);

    //? if <26 {
    /*public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(Laseredstone.id("gamerule_category"), Component.translatable("gamerule.category.laseredstone").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
    *///?} else {
    public static final GameRuleCategory CATEGORY = GameRuleCategory.register(Laseredstone.id("gamerule_category"));
    //?}

    //~ if >=1.21.11 'REGISTRANT.registerBoolean' -> 'REGISTRANT.register'
    public static final /*? <1.21.11 {*/ /*GameRules.Key<GameRules.BooleanValue> *//*?} else {*/ GameRule<Boolean> /*?}*/  MULTI_DAMAGE = REGISTRANT.register(
            "take_damage_from_multiple_lasers",
            //? if <1.21.11 {
            /*CATEGORY,
            true
            *///?} else {
            GameRuleBuilder.forBoolean(true).category(CATEGORY)
            //?}
    );

    public static void init() {
        // NO-OP
    }
}
