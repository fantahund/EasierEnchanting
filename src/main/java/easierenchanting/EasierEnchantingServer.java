package easierenchanting;

import net.fabricmc.api.DedicatedServerModInitializer;

public class EasierEnchantingServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        EasierEnchanting.init();
    }
}