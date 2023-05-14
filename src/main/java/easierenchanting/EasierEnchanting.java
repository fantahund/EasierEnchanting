package easierenchanting;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class EasierEnchanting implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "easierenchanting";
    public static final String MOD_NAME = "Easier Enchanting";

    public static int lapiscost = 6;
    public static int levelcost = 3;
    public static boolean uselevel = false;
    public static boolean enablereroll = true;
    public static boolean enablefulltext = true;

    @Override
    public void onInitialize() {

        log(Level.INFO, "Initializing..");
        try {
            Path path = Path.of("config/easierenchanting.txt");
            if (!Files.exists(path)) {
                log(Level.INFO, "config not found");
                log(Level.INFO, "creating new config file");

                Collection<String> collection = new ArrayList<>();
                collection.add("lapiscost:6");
                collection.add("levelcost:3");
                collection.add("uselevel:false");
                collection.add("enablereroll:true");
                collection.add("enablefulltext:true");
                Files.write(path, collection);
            }
            for (String s : Files.readAllLines(path)) {
                String[] tokens = s.split(":");
                switch (tokens[0].trim()) {
                    case "lapiscost" -> lapiscost = Math.max(0, Integer.parseInt(tokens[1].trim()));
                    case "levelcost" -> levelcost = Math.max(0, Integer.parseInt(tokens[1].trim()));
                    case "uselevel" -> uselevel = Boolean.parseBoolean(tokens[1].trim());
                    case "enablereroll" -> enablereroll = Boolean.parseBoolean(tokens[1].trim());
                    case "enablefulltext" -> enablefulltext = Boolean.parseBoolean(tokens[1].trim());
                }

                if (!enablereroll) {
                    log(Level.INFO, "reroll disabled");
                } else if (uselevel) {
                    log(Level.INFO, "setting level cost to " + levelcost);
                } else {
                    log(Level.INFO, "setting lapis cost to " + lapiscost);
                }
            }
        } catch (IOException e) {
            log(Level.ERROR, e.getMessage());
        }
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

}