package host.leak.togglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class ModEventHandler {
    private static KeyBinding sneakBinding;
    private static KeyBinding sprintBinding;

    public static void initKeybinds() {
        sneakBinding = new KeyBinding("togglesneak.key.toggle.sneak", Keyboard.KEY_G, "togglesneak.key.categories");
        sprintBinding = new KeyBinding("togglesneak.key.toggle.sprint", Keyboard.KEY_V, "togglesneak.key.categories");
        ClientRegistry.registerKeyBinding(sneakBinding);
        ClientRegistry.registerKeyBinding(sprintBinding);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) return;

        if(sneakBinding.isKeyDown()) ModConfig.toggleSneak = !ModConfig.toggleSneak;
        if(sprintBinding.isKeyDown()) ModConfig.toggleSprint = !ModConfig.toggleSprint;
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if ((player != null) && (!(player.movementInput instanceof ModMovementInput))) {
            ModMovementInput mim = new ModMovementInput();
            player.movementInput = mim;
            MinecraftForge.EVENT_BUS.register(new ModGui(mim));
        }
    }
}
