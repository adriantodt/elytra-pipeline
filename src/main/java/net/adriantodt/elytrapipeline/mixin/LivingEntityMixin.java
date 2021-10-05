package net.adriantodt.elytrapipeline.mixin;

import net.adriantodt.elytrapipeline.api.FallFlyingEventCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean isFallFlying();

    /**
     * @author AdrianTodt
     */
    @Overwrite
    private void tickFallFlying() {
        /*
         * This function is marked as @Overwrite. It might be possible to replicate this by using a @Redirect or a
         * cancellable @Inject, avoiding the use of an @Overwrite mixin.
         * TODO possibly turn LivingEntityMixin#tickFallFlying into a @Redirect or @Inject?
         * CAVEAT: Maybe letting @Overwrite would make people understand that mixins is not the path.
         * This function is a re-implementation of Minecraft's code, accounting for custom fall-flying sources.
         */
        boolean fallFlyingFlag = this.getFlag(Entity.FALL_FLYING_FLAG_INDEX);
        /*
         * It might be possible to make this function server-side only.
         * TODO investigate if LivingEntityMixin#tickFallFlying can be turned server-side only.
         */
        if (!fallFlyingFlag || !checkFallFlyingConditions() || !canElytraFly()) {
            fallFlyingFlag = false;
        }

        if (!this.world.isClient) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, fallFlyingFlag);
        }
    }

    public boolean canElytraFly() {
        /*
         * This function exists mainly for development readability, and might be inlined later.
         * TODO possibly inline LivingEntityMixin#canElytraFly function later.
         */
        return FallFlyingEventCallback.FLIGHT_SOURCES.invoker().canFallFly((LivingEntity) (Object) this);
    }

    protected boolean checkFallFlyingConditions() {
        return FallFlyingEventCallback.FLIGHT_CONDITIONS.invoker().canFallFly((LivingEntity) (Object) this)
            || FallFlyingEventCallback.FLIGHT_CONDITIONS_OVERRIDE.invoker().canFallFly((LivingEntity) (Object) this);
    }
}
