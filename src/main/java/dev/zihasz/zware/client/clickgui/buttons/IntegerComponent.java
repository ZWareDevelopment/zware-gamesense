package dev.zihasz.zware.client.clickgui.buttons;

import java.math.RoundingMode;
import java.math.BigDecimal;

import dev.zihasz.zware.api.settings.Setting;
import dev.zihasz.zware.api.util.font.FontUtils;
import dev.zihasz.zware.client.clickgui.frame.Buttons;
import dev.zihasz.zware.client.clickgui.frame.Component;
import dev.zihasz.zware.client.clickgui.frame.Renderer;
import dev.zihasz.zware.client.module.modules.gui.ColorMain;
import com.mojang.realmsclient.gui.ChatFormatting;

public class IntegerComponent extends Component {
	private boolean hovered;
	private final Setting.Integer set;
	private final Buttons parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging;
	private double renderWidth;
	
	public IntegerComponent(final Setting.Integer value, final Buttons button, final int offset){
		this.dragging = false;
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent(){
		Renderer.drawSliderBox(false, this.parent.parent.getX() + (int) this.renderWidth, this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 17, Renderer.getSettingColor(hovered));
		Renderer.drawSliderBox(true, this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 17, Renderer.getEnabledColor(hovered));
		FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
	}
	
	@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}
	
	@Override
	public void updateComponent(final int mouseX, final int mouseY){
		this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
		final double diff = Math.min(100, Math.max(0, mouseX - this.x));
		final int min = this.set.getMin();
		final int max = this.set.getMax();
		this.renderWidth = 99 * (this.set.getValue() - min) / (max - min);
		if (this.dragging){
			if (diff == 0.0){
				this.set.setValue(this.set.getMin());
			}
			else{
				final int newValue = (int)roundToPlace(diff / 100.0 * (max - min) + min, 2);
				this.set.setValue(newValue);
			}
		}
	}
	
	private static double roundToPlace(final double value, final int places){
		if (places < 0){
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button){
		if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open){
			this.dragging = true;
		}
		if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open){
			this.dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton){
		this.dragging = false;
	}
	
	public boolean isMouseOnButtonD(final int x, final int y){
		return x > this.x && x < this.x + (this.parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 16;
	}
	
	public boolean isMouseOnButtonI(final int x, final int y){
		return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 16;
	}
}