package easierenchanting;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EasierEnchanting implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing..");
        Config.deserialize();

        Events events = new Events();
        events.init();
    }
}