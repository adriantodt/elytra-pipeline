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

        if (this.checkFallFlying()) {
            this.networkHandler.sendPacket(
                new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING)
            );
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
         * This function exists to disable the vanilla Elytra path on tickMovement.
         */
        return false;
    }
}
