package easierenchanting;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    public static int lapisCost = 6;
    public static int levelCost = 3;
    public static boolean useLevel = false;
    public static boolean enableReroll = true;
    public static boolean enableFulltext = true;

    static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("easierenchanting.properties");

    public static void serialize() {
        Properties prop = new Properties();
        prop.setProperty("lapisCost", Integer.toString(lapisCost));
        prop.setProperty("levelCost", Integer.toString(levelCost));
        prop.setProperty("useLevel", Boolean.toString(useLevel));
        prop.setProperty("enableReroll", Boolean.toString(enableReroll));
        prop.setProperty("enableFulltext", Boolean.toString(enableFulltext));
        try {
            OutputStream s = Files.newOutputStream(configPath);
            prop.store(s, "EasierEnchanting Config");
            s.close();
        } catch (IOException e) {
            EasierEnchanting.LOGGER.warn("Failed to write config!");
        }
    }

    public static void deserialize() {
        deserialize(true);
    }

    public static void deserialize(boolean save) {
        Properties prop = new Properties();
        try {
            InputStream s = Files.newInputStream(configPath);
            prop.load(s);
            lapisCost = Integer.parseInt(prop.getProperty("lapisCost", "6"));
            levelCost = Integer.parseInt(prop.getProperty("levelCost", "3"));
            useLevel = Boolean.parseBoolean(prop.getProperty("useLevel", "false"));
            enableReroll = Boolean.parseBoolean(prop.getProperty("enableReroll", "true"));
            enableFulltext = Boolean.parseBoolean(prop.getProperty("enableFulltext", "true"));


            EasierEnchanting.LOGGER.log(Level.INFO, "reroll " + (enableReroll ? "enabled" : "disabled"));
            EasierEnchanting.LOGGER.log(Level.INFO, "fulltext " + (enableFulltext ? "enabled" : "disabled"));
            EasierEnchanting.LOGGER.log(Level.INFO, "setting " + (useLevel ? "level" : "lapis") + " cost to " + (useLevel ? levelCost : lapisCost));
        } catch (IOException e) {
            EasierEnchanting.LOGGER.warn("Failed to read config!");
        }
        if (save) {
            Config.serialize();
        }
    }

    public static void loadDefaults() {
        deserialize(false);
    }
}
