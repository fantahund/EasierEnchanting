package easierenchanting;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class Events {

    public Events() {
    }

    public void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, minecraftClient) -> Config.loadDefaults());
    }
}
