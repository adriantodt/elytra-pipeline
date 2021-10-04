package net.adriantodt.elytrapipeline.mixin;

import net.adriantodt.elytrapipeline.api.client.cape.AllowCapeRenderCallback;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        ),
        cancellable = true
    )
    void maybeHideCape(
        MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
        AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l,
        CallbackInfo callbackInfo
    ) {
        if (!AllowCapeRenderCallback.EVENT.invoker().canRenderCape(abstractClientPlayerEntity)) {
            callbackInfo.cancel();
        }
    }
}
