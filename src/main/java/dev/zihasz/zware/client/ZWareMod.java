package dev.zihasz.zware.client;

import java.awt.Font;

import dev.zihasz.zware.api.config.ConfigStopper;
import dev.zihasz.zware.api.config.LoadConfig;
import dev.zihasz.zware.api.config.SaveConfig;
import dev.zihasz.zware.api.event.EventProcessor;
import dev.zihasz.zware.api.settings.SettingsManager;
import dev.zihasz.zware.api.util.font.CFontRenderer;
import dev.zihasz.zware.api.util.players.enemy.Enemies;
import dev.zihasz.zware.api.util.players.friends.Friends;
import dev.zihasz.zware.api.util.render.CapeUtils;
import dev.zihasz.zware.client.clickgui.ClickGUI;
import dev.zihasz.zware.client.command.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import dev.zihasz.zware.client.module.ModuleManager;

import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ZWareMod.MODID, name = ZWareMod.FORGENAME, version = ZWareMod.MODVER, clientSideOnly = true)
public class ZWareMod {

	public static final String MODID = "zwaredotcc";
	public static String MODNAME = "ZWare.cc";
	public static final String FORGENAME = "ZWare.cc";
	public static final String MODVER = "v0.2.0";

	public static final Logger log = LogManager.getLogger(MODNAME);

	public EventProcessor eventProcessor;
	public SaveConfig saveConfig;
	public LoadConfig loadConfig;
	public ModuleManager moduleManager;
	public SettingsManager settingsManager;
	public static CFontRenderer fontRenderer;
	public CapeUtils capeUtils;
	public ClickGUI clickGUI;
	public Friends friends;
	public Enemies enemies;

	public static final EventBus EVENT_BUS = new EventManager();

	@Mod.Instance
	private static ZWareMod INSTANCE;

	public ZWareMod(){
		INSTANCE = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
		eventProcessor = new EventProcessor();
		eventProcessor.init();
		log.info("Events initialized!");

		fontRenderer = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true,true);
		log.info("Custom font initialized!");

		settingsManager = new SettingsManager();
		log.info("Settings initialized!");

		friends = new Friends();
		enemies = new Enemies();
		log.info("Friends and enemies initialized!");

		moduleManager = new ModuleManager();
		log.info("Modules initialized!");

		clickGUI = new ClickGUI();
		log.info("ClickGUI initialized!");

		CommandManager.initCommands();
		log.info("Commands initialized!");

		saveConfig = new SaveConfig();
		loadConfig = new LoadConfig();
		Runtime.getRuntime().addShutdownHook(new ConfigStopper());
		log.info("Config initialized!");

		log.info("Initialization complete!\n");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Display.setTitle(MODNAME + " " + MODVER);

		capeUtils = new CapeUtils();
		log.info("Capes initialised!");

		log.info("PostInitialization complete!\n");
	}

	public static ZWareMod getInstance(){
		return INSTANCE;
	}
}