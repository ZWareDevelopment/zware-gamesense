package dev.zihasz.zware.client.module.modules.misc;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.client.module.Module;
import dev.zihasz.zware.client.notification.Notification;
import dev.zihasz.zware.client.notification.NotificationManager;

import java.util.ArrayList;

public class NotificationTest extends Module {
    Setting.Mode type;
    Setting.Integer time;

    public NotificationTest() {
        super("NotifTest", Category.Misc);
    }

    @Override
    public void setup() {
        ArrayList<String> types = new ArrayList<>();
        types.add("Info");
        types.add("Alert");
        types.add("Warning");

        type = registerMode("Type", "NotifTestType", types, "Info");
        time = registerInteger("Time","NotifTestTime", 5, 1,10);
    }

    @Override
    protected void onEnable() {
        switch (type.getValue()) {
            case "Info":
                //NotificationManager.show(new Notification(Notification.NotificationType.INFO, "test","test on", time.getValue()));
                break;
            case "Alert":
                //NotificationManager.show(new Notification(Notification.NotificationType.ALERT, "test","test on", time.getValue()));
                break;
            case "Warning":
                //NotificationManager.show(new Notification(Notification.NotificationType.WARNING, "test","test on", time.getValue()));
                break;
        }
        
        this.disable();
    }
}
