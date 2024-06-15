package easierenchanting;

import net.fabricmc.api.ClientModInitializer;

public class EasierEnchantingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EasierEnchanting.init();
        new SettingsChannelHandler();
    }
}