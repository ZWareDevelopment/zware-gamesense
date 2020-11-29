package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.event.events.PacketEvent;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class ChatDownloader extends Module {
    public ChatDownloader() {
        super("ChatDownloader", Category.Misc);
    }

    Path folderPath;
    Path filePath;

    File file;
    Charset cs = StandardCharsets.UTF_8;;

    String fileName = "ZWare.cc/";
    String cdlName = "ChatDownloads/";

    ArrayList<String> lines = new ArrayList<String>();

    public void setup() {
        folderPath = Paths.get(fileName + cdlName);
        filePath = Paths.get(fileName + cdlName + "ChatDownload_" + mc.serverName + "_" + LocalDate.now());
    }

    public void onEnable() {
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (Exception ignored) {}
    }

    public  void onDisable() {
        try {
            Files.write(filePath, lines, cs, WRITE, CREATE);
        } catch (Exception ignored) {}
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            lines.add("[" + LocalDate.now() + " " + LocalTime.now() + "] " + ((CPacketChatMessage) event.getPacket()).message);
        }
    });
}
