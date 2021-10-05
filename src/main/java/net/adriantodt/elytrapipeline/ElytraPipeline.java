package net.adriantodt.elytrapipeline;

import net.adriantodt.elytrapipeline.api.FallFlyingEventCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.event.GameEvent;

public class ElytraPipeline implements ModInitializer {
    static {
        initVanilla();
    }

    private static void initVanilla() {
        FallFlyingEventCallback.FLIGHT_CONDITIONS.register(ElytraPipeline::vanillaFlightConditions);
        FallFlyingEventCallback.FLIGHT_START_CONDITIONS.register(ElytraPipeline::vanillaFlightStartConditions);
        FallFlyingEventCallback.FLIGHT_SOURCES.register(ElytraPipeline::elytraFlightSource);
    }

    private static boolean vanillaFlightConditions(LivingEntity livingEntity) {
        return !livingEntity.isOnGround()
            && !livingEntity.hasVehicle()
            && !livingEntity.hasStatusEffect(StatusEffects.LEVITATION);
    }

    private static boolean vanillaFlightStartConditions(LivingEntity livingEntity) {
        return !livingEntity.isFallFlying()
            && !livingEntity.isTouchingWater();
    }

    private static boolean elytraFlightSource(LivingEntity livingEntity) {
        ItemStack stack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (!stack.isOf(Items.ELYTRA) || !ElytraItem.isUsable(stack)) {
            return false;
        }
        int nextRoll = livingEntity.getRoll() + 1;
        if (!livingEntity.world.isClient && nextRoll % 10 == 0) {
            if (nextRoll / 10 % 2 == 0) {
                stack.damage(1, livingEntity, it -> it.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
            }
            livingEntity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
        }
        return true;
    }

    @Override
    public void onInitialize() {
    }
}
