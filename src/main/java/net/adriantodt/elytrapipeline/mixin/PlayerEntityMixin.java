package net.adriantodt.elytrapipeline.mixin;

import net.adriantodt.elytrapipeline.api.FallFlyingEventCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void startFallFlying();

    /**
     * @author AdrianTodt
     */
    @Overwrite
    public boolean checkFallFlying() {
        /*
         * This function is marked as @Overwrite. It might be possible to replicate this by using a @Redirect or a
         * cancellable @Inject, avoiding the use of an @Overwrite mixin.
         * TODO possibly turn PlayerEntityMixin#checkFallFlying into a @Redirect or @Inject?
         * CAVEAT: Maybe letting @Overwrite would make people understand that mixins is not the path.
         * Contractually, this method is called only on the first time the player tries to fly.
         */
        if (meetFallFlyingStartConditions() && this.canElytraFly()) { // this.canElytraFly is from parent mixin.
            this.startFallFlying();
            return true;
        }
        return false;
    }

    private boolean meetFallFlyingStartConditions() {
        return FallFlyingEventCallback.FLIGHT_START_CONDITIONS.invoker().canFallFly((PlayerEntity) (Object) this)
            && this.checkFallFlyingConditions();
    }
}
