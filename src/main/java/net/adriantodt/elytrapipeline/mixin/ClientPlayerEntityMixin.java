package net.adriantodt.elytrapipeline.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntityMixin {
    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    public ClientPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        ),
        method = "tickMovement"
    )
    public void patchClientControls(CallbackInfo info) {
        /*
         * This function re-implements the "pipeline" code for handling the client control to start fall flying.
         */
        if (canClientStartFallFlying()) {
            sendStartFallFlyingPacket();
        }
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ElytraItem;isUsable(Lnet/minecraft/item/ItemStack;)Z"
        ),
        method = "tickMovement"
    )
    public boolean disableVanillaElytraCode(ItemStack stack) {
        /*
         * This function exists to disable the vanilla Elytra check-and-enable path.
         * FIXME are we keeping Minecraft's Elytra check-and-enable path or re-enabling it?
         */
        return false;
    }

    private void sendStartFallFlyingPacket() {
        /*
         * This function exists solely for development readability, and can be inlined.
         * FIXME inline ClientPlayerEntityMixin#sendStartFallFlyingPacket function later.
         */
        this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    private boolean canClientStartFallFlying() {
        /*
         * This function exists mainly for development readability, and might be inlined later.
         * TODO possibly inline ClientPlayerEntityMixin#canClientStartFallFlying function later.
         * Currently, this function mimics Minecraft's default code.
         * Contractually, this function must return false OR this.checkFallFlying's return value.
         * TODO should we add support for more client-level pre-conditions? Uses include a lock keybind, a curse, status effect, etc.
         */
        return /* true && */ this.checkFallFlying();
    }
}
