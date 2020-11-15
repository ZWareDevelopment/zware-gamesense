package dev.zihasz.zware.client.clickgui.buttons;

import dev.zihasz.zware.api.util.font.FontUtils;
import dev.zihasz.zware.client.clickgui.frame.Buttons;
import dev.zihasz.zware.client.clickgui.frame.Component;
import dev.zihasz.zware.client.clickgui.frame.Renderer;
import dev.zihasz.zware.client.module.modules.gui.ClickGuiModule;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;

public class KeybindComponent extends Component {
	private boolean hovered;
	private boolean binding;
	private final Buttons parent;
	private int offset;
	private int x;
	private int y;
	
	public KeybindComponent(final Buttons button, final int offset){
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}
	
	@Override
	public void renderComponent(){
		Renderer.drawModuleBox(this.parent.parent.getX(), this.parent.parent.getY() + 1 + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, Renderer.getSettingColor(hovered));
		Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, ClickGuiModule.outlineColor.getValue().getRGB());
		FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), this.binding ? "Key..." : ("Key: " + ChatFormatting.GRAY + Keyboard.getKeyName(this.parent.mod.getBind())), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
	}
	
	@Override
	public void updateComponent(final int mouseX, final int mouseY){
		this.hovered = this.isMouseOnButton(mouseX, mouseY);
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button){
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open){
			this.binding = !this.binding;
		}
	}
	
	@Override
	public void keyTyped(final char typedChar, final int key){
		if (this.binding){
			if (key == 211){
				this.parent.mod.setBind(0);
			}
			else{
				if (key == Keyboard.KEY_ESCAPE){
					this.binding = false;
				}
				else{
					this.parent.mod.setBind(key);
				}
			}
			this.binding = false;
		}
	}
	
	public boolean isMouseOnButton(final int x, final int y){
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16;
	}
}
