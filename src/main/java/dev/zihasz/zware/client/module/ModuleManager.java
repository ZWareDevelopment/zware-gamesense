package dev.zihasz.zware.client.module;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dev.zihasz.zware.api.event.events.RenderEvent;
import dev.zihasz.zware.api.util.render.GameSenseTessellator;
import dev.zihasz.zware.client.module.modules.combat.*;
import dev.zihasz.zware.client.module.modules.misc.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.lwjgl.input.Keyboard;

import dev.zihasz.zware.client.module.modules.exploits.CoordExploit;
import dev.zihasz.zware.client.module.modules.exploits.FastBreak;
import dev.zihasz.zware.client.module.modules.exploits.LiquidInteract;
import dev.zihasz.zware.client.module.modules.exploits.NoInteract;
import dev.zihasz.zware.client.module.modules.exploits.NoSwing;
import dev.zihasz.zware.client.module.modules.exploits.PortalGodMode;
import dev.zihasz.zware.client.module.modules.gui.ClickGuiModule;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;
import dev.zihasz.zware.client.module.modules.hud.ArmorHUD;
import dev.zihasz.zware.client.module.modules.hud.CombatInfo;
import dev.zihasz.zware.client.module.modules.hud.InventoryViewer;
import dev.zihasz.zware.client.module.modules.hud.ModuleArrayList;
import dev.zihasz.zware.client.module.modules.hud.Notifications;
import dev.zihasz.zware.client.module.modules.hud.Overlay;
import dev.zihasz.zware.client.module.modules.hud.PotionEffects;
import dev.zihasz.zware.client.module.modules.hud.TargetHUD;
import dev.zihasz.zware.client.module.modules.hud.TextRadar;
import dev.zihasz.zware.client.module.modules.movement.Anchor;
import dev.zihasz.zware.client.module.modules.movement.Blink;
import dev.zihasz.zware.client.module.modules.movement.HoleTP;
import dev.zihasz.zware.client.module.modules.movement.PlayerTweaks;
import dev.zihasz.zware.client.module.modules.movement.ReverseStep;
import dev.zihasz.zware.client.module.modules.movement.Speed;
import dev.zihasz.zware.client.module.modules.movement.Sprint;
import dev.zihasz.zware.client.module.modules.movement.Step;
import dev.zihasz.zware.client.module.modules.render.BlockHighlight;
import dev.zihasz.zware.client.module.modules.render.CapesModule;
import dev.zihasz.zware.client.module.modules.render.CityESP;
import dev.zihasz.zware.client.module.modules.render.ESP;
import dev.zihasz.zware.client.module.modules.render.Freecam;
import dev.zihasz.zware.client.module.modules.render.Fullbright;
import dev.zihasz.zware.client.module.modules.render.HitSpheres;
import dev.zihasz.zware.client.module.modules.render.HoleESP;
import dev.zihasz.zware.client.module.modules.render.LogoutSpots;
import dev.zihasz.zware.client.module.modules.render.Nametags;
import dev.zihasz.zware.client.module.modules.render.NoRender;
import dev.zihasz.zware.client.module.modules.render.RenderTweaks;
import dev.zihasz.zware.client.module.modules.render.ShulkerViewer;
import dev.zihasz.zware.client.module.modules.render.SkyColor;
import dev.zihasz.zware.client.module.modules.render.Tracers;
import dev.zihasz.zware.client.module.modules.render.ViewModel;
import dev.zihasz.zware.client.module.modules.render.VoidESP;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
	public static ArrayList<Module> modules;

	public ModuleManager(){
		modules = new ArrayList<>();
		//Combat
		addMod(new AutoArmor());
		addMod(new AutoCrystal());
		addMod(new AutoLog());
		addMod(new AutoTotem());
		addMod(new AutoTrap());
		addMod(new AutoWeb());
		addMod(new BedBomb());
		addMod(new FastBow());
		addMod(new FootExp());
		addMod(new HoleFill());
		addMod(new KillAura());
		addMod(new OffhandCrystal());
		addMod(new OffhandGap());
		addMod(new PacketEXP());
		addMod(new Quiver());
		addMod(new SelfTrap());
		addMod(new SelfWeb());
		addMod(new SmartOffhand());
		addMod(new Surround());
		//Exploits
		addMod(new CoordExploit());
		addMod(new FastBreak());
		addMod(new LiquidInteract());
		addMod(new NoInteract());
		addMod(new NoSwing());
		addMod(new PortalGodMode());
		//Movement
		addMod(new Anchor());
		addMod(new Blink());
		addMod(new HoleTP());
		addMod(new PlayerTweaks());
		addMod(new ReverseStep());
		addMod(new Speed());
		addMod(new Sprint());
		addMod(new Step());
		//Misc
		addMod(new Announcer());
		addMod(new AutoGG());
		addMod(new AutoPorn());
		addMod(new AutoReply());
		addMod(new AutoTool());
		addMod(new ChatModifier());
		addMod(new ChatSuffix());
		addMod(new DiscordRPCModule());
		addMod(new FastPlace());
		addMod(new FakePlayer());
		addMod(new HoosiersDupe());
		addMod(new HotbarRefill());
		addMod(new MCF());
		addMod(new MultiTask());
		addMod(new NoEntityTrace());
		addMod(new NoKick());
		addMod(new NotificationTest());
		addMod(new PvPInfo());
		//Render
		addMod(new BlockHighlight());
		addMod(new CapesModule());
		addMod(new CityESP());
		addMod(new ESP());
		addMod(new Freecam());
		addMod(new Fullbright());
		addMod(new HitSpheres());
		addMod(new HoleESP());
		addMod(new LogoutSpots());
		addMod(new Nametags());
		addMod(new NoRender());
		addMod(new RenderTweaks());
		addMod(new ShulkerViewer());
		addMod(new SkyColor());
		addMod(new Tracers());
		addMod(new ViewModel());
		addMod(new VoidESP());
		//HUD
		addMod(new ArmorHUD());
		addMod(new ModuleArrayList());
		addMod(new CombatInfo());
		addMod(new InventoryViewer());
		addMod(new Notifications());
		addMod(new Overlay());
		addMod(new PotionEffects());
		addMod(new TargetHUD());
		addMod(new TextRadar());
		//GUI
		addMod(new ClickGuiModule());
		addMod(new ColorMain());
	}

	public static void addMod(Module m){
		modules.add(m);
	}

	public static void onUpdate() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
	}

	public static void onRender() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
	}

	public static void onWorldRender(RenderWorldLastEvent event) {
		Minecraft.getMinecraft().profiler.startSection("gamesense");
		Minecraft.getMinecraft().profiler.startSection("setup");
		GameSenseTessellator.prepare();
		RenderEvent e = new RenderEvent(event.getPartialTicks());
		Minecraft.getMinecraft().profiler.endSection();

		modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
			Minecraft.getMinecraft().profiler.startSection(module.getName());
			module.onWorldRender(e);
			Minecraft.getMinecraft().profiler.endSection();
		});

		Minecraft.getMinecraft().profiler.startSection("release");
		GameSenseTessellator.release();
		Minecraft.getMinecraft().profiler.endSection();
		Minecraft.getMinecraft().profiler.endSection();
	}

	public static ArrayList<Module> getModules() {
		return modules;
	}

	public static ArrayList<Module> getModulesInCategory(Module.Category c){
		ArrayList<Module> list = (ArrayList<Module>) getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
		return list;
	}

	public static void onBind(int key) {
		if (key == 0 || key == Keyboard.KEY_NONE) return;
		modules.forEach(module -> {
			if(module.getBind() == key){
				module.toggle();
			}
		});
	}

	public static Module getModuleByName(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m;
	}

	public static boolean isModuleEnabled(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m.isEnabled();
	}

	public static boolean isModuleEnabled(Module m){
		return m.isEnabled();
	}

	public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3d(
				(entity.posX - entity.lastTickPosX) * x,
				(entity.posY - entity.lastTickPosY) * y,
				(entity.posZ - entity.lastTickPosZ) * z
		);
	}
}