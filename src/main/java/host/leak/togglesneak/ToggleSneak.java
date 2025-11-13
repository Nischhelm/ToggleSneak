package host.leak.togglesneak;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = ToggleSneak.MODID,
        name = ToggleSneak.NAME,
        version = ToggleSneak.VERSION,
        clientSideOnly = true
)
public class ToggleSneak {
    public static final String MODID = "togglesneak";
    public static final String NAME = "Toggle Sneak&Sprint";
    public static final String VERSION = "1.1";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModEventHandler.initKeybinds();
    }
}
