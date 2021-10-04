package net.adriantodt.elytrapipeline.api.client.cape;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public interface AllowCapeRenderCallback {
    Event<AllowCapeRenderCallback> EVENT = EventFactory.createArrayBacked(
        AllowCapeRenderCallback.class,
        (player) -> true,
        (callbacks) -> (player) -> {
            for (AllowCapeRenderCallback callback : callbacks) {
                if (!callback.canRenderCape(player)) {
                    return false;
                }
            }
            return true;
        }
    );

    // Called when the player has a cape
    // Return false to hide the player's cape, if the player has one
    boolean canRenderCape(AbstractClientPlayerEntity player);
}
