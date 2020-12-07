package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.util.misc.Text;
import dev.zihasz.zware.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.ClientChatEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;


public class ChatDownloader extends Module {
    public ChatDownloader() {
        super("ChatDownloader", Category.Misc);
    }

    boolean existed = false;

    String path = "ZWare.cc/ChatDownloads/";

    File file;
    FileWriter writer;

    public void setup() {

    }

    protected void onEnable() {
        try {
            String fullPath = path + "ChatDownload_" + LocalDate.now() + ".txt";
            file = new File(fullPath);
            existed = !file.createNewFile();
            writer = new FileWriter(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onDisable() {
        try {
            // writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    private final Listener<ClientChatEvent> onChatReceived = new Listener<>(event -> {
        try {
            writer.append(Text.formatForChatDownload(mc.serverName + ":" + mc.serverPort)).append(" ").append(event.getMessage()).append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
}
