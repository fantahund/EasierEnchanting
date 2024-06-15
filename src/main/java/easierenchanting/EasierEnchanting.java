package easierenchanting;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EasierEnchanting {

    public static Logger LOGGER = LogManager.getLogger();
    public static final Identifier CHANNEL_IDENTIFIER = new Identifier("easierenchanting", "config");

    public static void init() {
        LOGGER.log(Level.INFO, "Initializing..");
        Config.deserialize();

        Events events = new Events();
        events.init();
    }
}
