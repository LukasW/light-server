package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.common.LightControl;
import ch.smaug.light.server.control.fading.FadingLightControl;

@ApplicationScoped
public class MasterLightControl implements LightControl {

	@Inject
	private FadingLightControl fadingLightControl;

	private int level;

	@Override
	public int getNumberOfLevels() {
		return fadingLightControl.getNumberOfLevels();
	}

	public void turnOn() {
		fadingLightControl.setLevel(level);
	}

	public void turnOff() {
		fadingLightControl.setLevel(0);
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;
		fadingLightControl.setLevel(level);
	}

	public int getCurrentLevel() {
		return level;
	}

	public boolean isOn() {
		return level > 0;
	}
}
