package easierenchanting;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public class SettingsChannelHandler implements ClientPlayNetworking.PlayChannelHandler, ClientConfigurationNetworking.ConfigurationChannelHandler {

    private static final Identifier CHANNEL_IDENTIFIER = new Identifier("easierenchanting", "config");

    public SettingsChannelHandler() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_IDENTIFIER, this);
        ClientConfigurationNetworking.registerGlobalReceiver(CHANNEL_IDENTIFIER, this);
    }

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender responseSender) {
        parsePacket(buffer);
    }

    @Override
    public void receive(MinecraftClient client, ClientConfigurationNetworkHandler handler, PacketByteBuf buffer, PacketSender responseSender) {
        parsePacket(buffer);
    }

    private void parsePacket(PacketByteBuf buffer) {
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
    }
}
