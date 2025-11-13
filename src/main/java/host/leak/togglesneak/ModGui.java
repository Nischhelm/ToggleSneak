package host.leak.togglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModGui extends Gui {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModMovementInput MIM;
    private int mcDisplayWidth = -1, mcDisplayHeight = -1;
    private int rectX1, rectX2;
    private int rectSnY1, rectSnY2, rectSpY1, rectSpY2;
    private final String sprintTxt;
    private final String sneakTxt;

    public ModGui(ModMovementInput mIM) {
        this.MIM = mIM;
        this.sprintTxt = I18n.format("togglesneak.display.label.sprint");
        this.sneakTxt = I18n.format("togglesneak.display.label.sneak");
    }

    @SubscribeEvent
    public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() != ElementType.ALL) return;
        if (ModConfig.statusDisplay == ModConfig.DisplayStyle.COLOR_CODED) {
            computeDrawPosIfChanged();
            drawRect(rectX1, rectSnY1, rectX2, rectSnY2, ModConfig.toggleSneak ? getColorInt(0, 0, 196, 150) : getColorInt(196, 196, 196, 64));
            drawString(mc.fontRenderer, sneakTxt, rectX1 + 2, rectSnY1 + 2,
                    MIM.sneak ? getColorInt(255, 255, 0, 255) : getColorInt(64, 64, 64, 255));
            drawRect(rectX1, rectSpY1, rectX2, rectSpY2, ModConfig.toggleSprint ? getColorInt(0, 0, 196, 150) : getColorInt(196, 196, 196, 64));
            drawString(mc.fontRenderer, sprintTxt, rectX1 + 2, rectSpY1 + 2,
                    MIM.sprint ? getColorInt(255, 255, 0, 255) : getColorInt(64, 64, 64, 255));
        } else if (ModConfig.statusDisplay == ModConfig.DisplayStyle.TEXT) {
            String onlyTxt = MIM.displayText();
            computeTextPos(onlyTxt);
            ToggleSneak.GUI.drawString(mc.fontRenderer, onlyTxt, rectX1, rectSnY1, getColorInt(255, 255, 255, 192));
        }
    }

    public void computeDrawPosIfChanged() {
        if (!ModConfig.configChanged && (mcDisplayWidth == mc.displayWidth) && (mcDisplayHeight == mc.displayHeight)) return;
        ModConfig.configChanged = false;

        ScaledResolution scaledresolution = new ScaledResolution(mc);

        int displayWidth = scaledresolution.getScaledWidth();
        int textWidth = Math.max(mc.fontRenderer.getStringWidth(sprintTxt), mc.fontRenderer.getStringWidth(sneakTxt));

        switch (ModConfig.displayHPos){
            case RIGHT:
                rectX2 = displayWidth - 2;
                rectX1 = rectX2 - 2 - textWidth - 2;
                break;
            case CENTER:
                rectX1 = (displayWidth / 2) - (textWidth / 2) - 2;
                rectX2 = rectX1 + 2 + textWidth + 2;
                break;
            case LEFT:
                rectX1 = 2;
                rectX2 = rectX1 + 2 + textWidth + 2;
                break;
        }
        rectX1 += ModConfig.hOffset;
        rectX2 += ModConfig.hOffset;

        int displayHeight = scaledresolution.getScaledHeight();
        int textHeight = mc.fontRenderer.FONT_HEIGHT;

        switch (ModConfig.displayVPos){
            case BOTTOM:
                rectSpY2 = displayHeight - 2;
                rectSpY1 = rectSpY2 - 2 - textHeight - 2;
                rectSnY2 = rectSpY1 - 2;
                rectSnY1 = rectSnY2 - 2 - textHeight - 2;
                break;
            case MIDDLE:
                rectSnY1 = (displayHeight / 2) - 1 - 2 - textHeight - 2;
                rectSnY2 = rectSnY1 + 2 + textHeight + 2;
                rectSpY1 = rectSnY2 + 2;
                rectSpY2 = rectSpY1 + 2 + textHeight + 2;
                break;
            case TOP:
                rectSnY1 = 2;
                rectSnY2 = rectSnY1 + 2 + textHeight + 2;
                rectSpY1 = rectSnY2 + 2;
                rectSpY2 = rectSpY1 + 2 + textHeight + 2;
                break;
        }
        rectSnY1 += ModConfig.vOffset;
        rectSnY2 += ModConfig.vOffset;
        rectSpY1 += ModConfig.vOffset;
        rectSpY2 += ModConfig.vOffset;

        mcDisplayWidth = mc.displayWidth;
        mcDisplayHeight = mc.displayHeight;
    }

    public void computeTextPos(String displayTxt) {
        ScaledResolution scaledresolution = new ScaledResolution(mc);

        int displayWidth = scaledresolution.getScaledWidth();
        int textWidth = mc.fontRenderer.getStringWidth(displayTxt);

        switch (ModConfig.displayHPos){
            case RIGHT:
                rectX1 = displayWidth - textWidth - 2;
                break;
            case CENTER:
                rectX1 = (displayWidth / 2) - (textWidth / 2) - 2;
                break;
            case LEFT:
                rectX1 = 2;
                rectX2 = rectX1 + 2 + textWidth + 2;
                break;
        }
        rectX1 += ModConfig.hOffset;
        rectX2 += ModConfig.hOffset;

        int displayHeight = scaledresolution.getScaledHeight();
        int textHeight = mc.fontRenderer.FONT_HEIGHT;

        switch (ModConfig.displayVPos){
            case BOTTOM:
                rectSnY1 = displayHeight - 2;
                break;
            case MIDDLE:
                rectSnY1 = (displayHeight / 2) + textHeight / 2;
                break;
            case TOP:
                rectSnY1 = 2 + textHeight;
                break;
        }
        rectSnY1 += ModConfig.vOffset;
    }

    private int getColorInt(int red, int green, int blue, int alpha) {
        return ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255) | ((alpha & 255) << 24);
    }
}
