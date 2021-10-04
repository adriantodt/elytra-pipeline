package net.adriantodt.elytrapipeline.mixin;

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
        // This method is called only on the first time the player tries to fly.
        // Starting conditions are checked here. If we're able to fly, enables the flag.
        // TODO maybe turn this into a @Redirect or @Inject?
        // Maybe letting @Overwrite would make people understand that mixins is not the path.
        if (meetFallFlyingStartConditions() && this.canElytraFly()) { // canElytraFly is from parent mixin.
            this.startFallFlying();
            return true;
        }

        return false;
    }

    private boolean meetFallFlyingStartConditions() {
        // "Start" conditions are different from "continue" conditions.
        // TODO A way to add more conditions?
        return !this.isFallFlying()
            && !this.isTouchingWater()
            && this.checkFallFlyingConditions();
    }
}
