package host.leak.togglesneak;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ToggleSneak.MODID)
public class ModConfig {

    @Config.Comment("Will the sneak toggle function be enabled on startup?")
    @Config.Name("toggleSneakEnabled")
    @Config.LangKey("togglesneak.config.panel.sneak")
    public static boolean toggleSneak = true;

    @Config.Comment("Will the sprint toggle function be enabled on startup?")
    @Config.Name("toggleSprintEnabled")
    @Config.LangKey("togglesneak.config.panel.sprint")
    public static boolean toggleSprint = false;

    @Config.Comment("Will the player auto unsneak when starting to sprint?")
    @Config.Name("sprintUnsneaks")
    @Config.LangKey("togglesneak.config.panel.sprintunsneaks")
    public static boolean sprintUnsneaks = true;

    @Config.Comment("Fly boost activated by sprint key in creative mode")
    @Config.Name("flyBoostEnabled")
    @Config.LangKey("togglesneak.config.panel.flyboost")
    public static boolean flyBoost = false;

    @Config.Comment("Speed multiplier for fly boost")
    @Config.Name("flyBoostFactor")
    @Config.LangKey("togglesneak.config.panel.flyboostfactor")
    @Config.RangeDouble(min = 1, max = 16)
    @Config.SlidingOption
    public static float flyBoostFactor = 4.0F;

    @Config.Comment("Minimum key hold time in ticks to prevent toggle")
    @Config.Name("keyHoldTicks")
    @Config.LangKey("togglesneak.config.panel.keyholdticks")
    @Config.RangeInt(min = 0, max = 200)
    public static int keyHoldTicks = 7;

    @Config.Comment("Status display style")
    @Config.Name("statusDisplay")
    @Config.LangKey("togglesneak.config.panel.display")
    public static DisplayStyle statusDisplay = DisplayStyle.COLOR_CODED;

    @Config.Comment("Horizontal position of onscreen display")
    @Config.Name("displayHPosition")
    @Config.LangKey("togglesneak.config.panel.hpos")
    public static HPos displayHPos = HPos.LEFT;

    @Config.Comment("Vertical position of onscreen display")
    @Config.Name("displayVPosition")
    @Config.LangKey("togglesneak.config.panel.vpos")
    public static VPos displayVPos = VPos.MIDDLE;

    @Config.Comment("Horizontal offset of onscreen display")
    @Config.Name("displayHOffset")
    @Config.LangKey("togglesneak.config.panel.hoffset")
    @Config.SlidingOption
    @Config.RangeInt(min = -500, max = 500)
    public static int hOffset = 0;

    @Config.Comment("Vertical offset of onscreen display")
    @Config.Name("displayVOffset")
    @Config.LangKey("togglesneak.config.panel.voffset")
    @Config.SlidingOption
    @Config.RangeInt(min = -500, max = 500)
    public static int vOffset = 0;

    @Mod.EventBusSubscriber(modid = ToggleSneak.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (!(event.getModID().equals(ToggleSneak.MODID))) return;
            ConfigManager.sync(ToggleSneak.MODID, Config.Type.INSTANCE);
            configChanged = true;
        }
    }

    @Config.Ignore
    public static boolean configChanged = false;

    public enum DisplayStyle{DISABLED, COLOR_CODED, TEXT}
    public enum HPos{LEFT, CENTER, RIGHT}
    public enum VPos{TOP, MIDDLE, BOTTOM}
}
