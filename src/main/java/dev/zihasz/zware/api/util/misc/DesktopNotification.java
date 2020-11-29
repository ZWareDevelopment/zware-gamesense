package dev.zihasz.zware.api.util.misc;

import java.awt.*;

public class DesktopNotification {

    public void displayTray(String tooltip, String title, String message, TrayIcon.MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, tooltip);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(tooltip);
        tray.add(trayIcon);

        trayIcon.displayMessage(title, message, type);
    }

}
