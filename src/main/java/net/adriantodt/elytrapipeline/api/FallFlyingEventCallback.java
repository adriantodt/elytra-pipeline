package net.adriantodt.elytrapipeline.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface FallFlyingEventCallback {
    Event<FallFlyingEventCallback> FLIGHT_SOURCES = EventFactory.createArrayBacked(
        FallFlyingEventCallback.class,
        (entity) -> false,
        (callbacks) -> (entity) -> {
            for (FallFlyingEventCallback callback : callbacks) {
                if (callback.canFallFly(entity)) {
                    return true;
                }
            }
            return false;
        }
    );

    Event<FallFlyingEventCallback> FLIGHT_CONDITIONS = EventFactory.createArrayBacked(
        FallFlyingEventCallback.class,
        (entity) -> true,
        (callbacks) -> (entity) -> {
            for (FallFlyingEventCallback callback : callbacks) {
                if (!callback.canFallFly(entity)) {
                    return false;
                }
            }
            return true;
        }
    );

    Event<FallFlyingEventCallback> FLIGHT_CONDITIONS_OVERRIDE = EventFactory.createArrayBacked(
        FallFlyingEventCallback.class,
        (entity) -> false,
        (callbacks) -> (entity) -> {
            for (FallFlyingEventCallback callback : callbacks) {
                if (callback.canFallFly(entity)) {
                    return true;
                }
            }
            return false;
        }
    );

    Event<FallFlyingEventCallback> FLIGHT_START_CONDITIONS = EventFactory.createArrayBacked(
        FallFlyingEventCallback.class,
        (entity) -> true,
        (callbacks) -> (entity) -> {
            for (FallFlyingEventCallback callback : callbacks) {
                if (!callback.canFallFly(entity)) {
                    return false;
                }
            }
            return true;
        }
    );

    boolean canFallFly(LivingEntity entity);
}
