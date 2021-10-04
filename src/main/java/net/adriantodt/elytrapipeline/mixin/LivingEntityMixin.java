package net.adriantodt.elytrapipeline.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
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
         * This function is currently a stub, and returns true for debug purposes.
         * FIXME LivingEntityMixin#canElytraFly function is a stub.
         */
        return true;
    }

    protected boolean checkFallFlyingConditions() {
        /*
         * Currently, this function mimics Minecraft's default code.
         * TODO should we add support for more common conditions? Uses include a curse, status effect, etc.
         * (For client-side conditions, check ClientPlayerEntityMixin#canClientStartFallFlying.
         */
        return !this.onGround
            && !this.hasVehicle()
            && !this.hasStatusEffect(StatusEffects.LEVITATION);
    }
}
