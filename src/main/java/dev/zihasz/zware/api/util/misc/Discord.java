package dev.zihasz.zware.api.util.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import dev.zihasz.zware.client.ZWareMod;
import net.minecraft.client.Minecraft;

/**
 * @Author Hoosiers on 10/27/2020
 */

public class Discord {

    private static String discordID = "748610552569397309";
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    private static DiscordRichPresence rpc = new DiscordRichPresence();

    private static String clientVersion = ZWareMod.MODVER;

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        rpc.startTimestamp = System.currentTimeMillis() / 1000L;

        rpc.state = "Idle";
        rpc.details = "ZWare.cc" + clientVersion;

        rpc.largeImageKey = "large";
        rpc.largeImageText = "ZWare.cc on top!";

        // rpc.partyId = "";
        // rpc.partySize = 0;
        // rpc.partyMax = 10;

        // rpc.matchSecret = genSecret(mc.session.getToken());
        // rpc.joinSecret = "genSecret(mc.session.getToken())";
        // rpc.spectateSecret = "genSecret(mc.session.getToken())";

        rpc.smallImageKey = "small";
        rpc.smallImageText = mc.session.getUsername();

        discordRPC.Discord_UpdatePresence(rpc);
    }
    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
    public static void updateRPCState(String state) {
        rpc.state = state;
        discordRPC.Discord_UpdatePresence(rpc);
    }

    /*
    public static String genSecret(String token) {
        String s = "";

        s += token.hashCode();
        s += "-";
        s += System.getProperty("user.name").toLowerCase().hashCode();
        s += "-";
        s += Text.SHA512Hash(System.getProperty("os.arch"));

        return s;
    }
     */
}