package host.leak.togglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

import java.text.DecimalFormat;

public class ModMovementInput extends MovementInput {
    private final GameSettings gameSettings;
    private final Minecraft mc = Minecraft.getMinecraft();
    public boolean sprint;
    private int sneakWasPressed;
    private int sprintWasPressed;
    private float originalFlySpeed = -1.0F;
    private float boostedFlySpeed;

    public ModMovementInput() {
        gameSettings = Minecraft.getMinecraft().gameSettings;
        sprint = false;
        sneakWasPressed = 0;
        sprintWasPressed = 0;
    }

    public void updatePlayerMoveState() {
        EntityPlayerSP player = mc.player;
        moveStrafe = 0.0F;
        moveForward = 0.0F;

        forwardKeyDown = gameSettings.keyBindForward.isKeyDown();
        backKeyDown = gameSettings.keyBindBack.isKeyDown();
        leftKeyDown = gameSettings.keyBindLeft.isKeyDown();
        rightKeyDown = gameSettings.keyBindRight.isKeyDown();
        if (forwardKeyDown) moveForward++;
        if (backKeyDown) moveForward--;
        if (leftKeyDown) moveStrafe++;
        if (rightKeyDown) moveStrafe--;

        jump = gameSettings.keyBindJump.isKeyDown();

        handleSneakToggle();

        if (sneak) {
            moveStrafe *= 0.3F;
            moveForward *= 0.3F;
        }
        
        handleSprintToggle();

        // sprint conditions same as in net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()
        // check for hungry or flying. But nvm, if conditions not met, sprint will
        // be canceled there afterward anyway
        if (sprint && moveForward == 1.0F && player.onGround && !player.isHandActive()
                && !player.isPotionActive(MobEffects.BLINDNESS)) player.setSprinting(true);

        handleFlyBoost();
    }

    private void handleSneakToggle() {
        if (ModConfig.toggleSneak) {
            if (sneakKeyDown()) {
                if (sneakWasPressed == 0) {
                    if (sneak) sneakWasPressed = -1;
                    else if (mc.player.isRiding() || mc.player.capabilities.isFlying) sneakWasPressed = ModConfig.keyHoldTicks + 1;
                    else sneakWasPressed = 1;

                    sneak = !sneak;
                } else if (sneakWasPressed > 0) {
                    sneakWasPressed++;
                }
            } else {
                if (ModConfig.keyHoldTicks > 0 && sneakWasPressed > ModConfig.keyHoldTicks) sneak = false;
                sneakWasPressed = 0;
            }
        } else {
            sneak = sneakKeyDown();
        }
    }

    private void handleSprintToggle() {
        if (ModConfig.toggleSprint) {
            if (sprintKeyDown()) {
                if (sprintWasPressed == 0) {
                    if (sprint) sprintWasPressed = -1;
                    else sprintWasPressed = 1;

                    sprint = !sprint;

                    if(ModConfig.sprintUnsneaks && ModConfig.toggleSneak) sneak = false;
                } else if (sprintWasPressed > 0) {
                    sprintWasPressed++;
                }
            } else {
                if ((ModConfig.keyHoldTicks > 0) && (sprintWasPressed > ModConfig.keyHoldTicks)) sprint = false;
                sprintWasPressed = 0;
            }
        } else{
            sprint = false;
            if(sprintKeyDown() && sprintWasPressed == 0 && ModConfig.sprintUnsneaks && ModConfig.toggleSneak) sneak = false;
        }
    }

    private void handleFlyBoost() {
        if (ModConfig.flyBoost && mc.player.capabilities.isCreativeMode && mc.player.capabilities.isFlying
                && (mc.getRenderViewEntity() == mc.player) && sprint) {

            if (originalFlySpeed < 0.0F || mc.player.capabilities.getFlySpeed() != boostedFlySpeed)
                originalFlySpeed = mc.player.capabilities.getFlySpeed();
            boostedFlySpeed = originalFlySpeed * ModConfig.flyBoostFactor;
            mc.player.capabilities.setFlySpeed(boostedFlySpeed);

            if (sneak) mc.player.motionY -= 0.15D * (double) (ModConfig.flyBoostFactor - 1.0F);
            if (jump) mc.player.motionY += 0.15D * (double) (ModConfig.flyBoostFactor - 1.0F);

        } else {
            if (mc.player.capabilities.getFlySpeed() == boostedFlySpeed)
                mc.player.capabilities.setFlySpeed(originalFlySpeed);
            originalFlySpeed = -1.0F;
        }
    }

    public boolean sneakKeyDown(){
        return gameSettings.keyBindSneak.isKeyDown();
    }

    public boolean sprintKeyDown(){
        return gameSettings.keyBindSprint.isKeyDown();
    }

    public String displayText() {

        // This is a slightly refactored version of Deez's UpdateStatus( ... ) function
        // found here https://github.com/DouweKoopmans/ToggleSneak/blob/master/src/main/java/deez/togglesneak/CustomMovementInput.java

        String displayText = "";
        boolean isFlying = mc.player.capabilities.isFlying;
        boolean isRiding = mc.player.isRiding();
        boolean isHoldingSneak = sneakKeyDown();
        boolean isHoldingSprint = sprintKeyDown();

        if (isFlying) {
            if (originalFlySpeed > 0.0F) {
                displayText += "[Flying (" + (new DecimalFormat("#.0")).format(boostedFlySpeed / originalFlySpeed) + "x Boost)]  ";
            } else {
                displayText += "[Flying]  ";
            }
        }
        if (isRiding) displayText += "[Riding]  ";

        if (sneak) {

            if (isFlying) displayText += "[Descending]  ";
            else if (isRiding) displayText += "[Dismounting]  ";
            else if (isHoldingSneak) displayText += "[Sneaking (Key Held)]  ";
            else displayText += "[Sneaking (Toggled)]  ";

        } else if (sprint && !isRiding) {

            if (isHoldingSprint) displayText += "[Sprinting (Key Held)]";
            else displayText += "[Sprinting (Toggled)]";
        }

        return displayText.trim();
    }
}
