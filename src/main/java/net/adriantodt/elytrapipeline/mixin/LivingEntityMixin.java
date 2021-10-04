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
        // TODO maybe turn this into a @Redirect or @Inject?
        // Maybe letting @Overwrite would make people understand that mixins is not the path.
        boolean fallFlyingFlag = this.getFlag(Entity.FALL_FLYING_FLAG_INDEX);
        // TODO investigate if this checks could be server-side only.
        if (!fallFlyingFlag || !checkFallFlyingConditions() || !canElytraFly()) {
            fallFlyingFlag = false;
        }

        if (!this.world.isClient) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, fallFlyingFlag);
        }
    }

    public boolean canElytraFly() {
        // FIXME this is a stub.
        return true;
    }

    protected boolean checkFallFlyingConditions() {
        // TODO A way to add more conditions?
        return !this.onGround
            && !this.hasVehicle()
            && !this.hasStatusEffect(StatusEffects.LEVITATION);
    }
}
