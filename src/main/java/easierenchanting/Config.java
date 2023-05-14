package easierenchanting;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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

    public static void sendConfig(PacketSender sender) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        Map<String, Object> settings = new HashMap<>();
        settings.put("lapisCost", Config.lapisCost);
        settings.put("levelCost", Config.levelCost);
        settings.put("useLevel", Config.useLevel);
        settings.put("enableReroll", Config.enableReroll);
        settings.put("enableFulltext", Config.enableFulltext);
        packetByteBuf.writeString(new Gson().toJson(settings));
        sender.sendPacket(EasierEnchanting.CHANNEL_IDENTIFIER, packetByteBuf);
    }

    public static boolean handleCustomPayload(CustomPayloadS2CPacket packet) {
        PacketByteBuf buffer = packet.getData();
        String channelName = packet.getChannel().toString();
        if (channelName.equals("easierenchanting:config")) {
            String jsonString = buffer.readString();
            @SuppressWarnings("unchecked")
            Map<String, Object> settings = new Gson().fromJson(jsonString, Map.class);
            for (Map.Entry<String, Object> entry : settings.entrySet()) {
                String setting = entry.getKey();
                Object value = entry.getValue();
                switch (setting) {
                    case "lapisCost" -> {
                        Double data = (Double) value;
                        Config.lapisCost = data.intValue();
                    }
                    case "levelCost" -> {
                        Double data = (Double) value;
                        Config.levelCost = data.intValue();
                    }
                    case "useLevel" -> Config.useLevel = (Boolean) value;
                    case "enableReroll" -> Config.enableReroll = (Boolean) value;
                    case "enableFulltext" -> Config.enableFulltext = (Boolean) value;
                    default -> EasierEnchanting.LOGGER.warn("Unknown configuration option " + setting);
                }
            }
            return true;
        }
        return false;
    }
}
