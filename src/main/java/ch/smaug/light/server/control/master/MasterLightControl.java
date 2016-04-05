package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;

@ApplicationScoped
public class MasterLightControl {

	private static final int FULL_LIGHT = 100;
	private static final int NO_LIGHT = 0;

	@Inject
	private LinearizedLightControl lightControl;

	private int lightIntensityRatio = NO_LIGHT;
	private int lastLightIntensityRatio = FULL_LIGHT;
	private boolean dimUp;

	public void turnOn() {
		lightIntensityRatio = lastLightIntensityRatio;
		writeLightIntensityRatio();
	}

	public void fullLight() {
		lightIntensityRatio = FULL_LIGHT;
		lastLightIntensityRatio = lightIntensityRatio;
		writeLightIntensityRatio();
	}

	public void turnOff() {
		lightIntensityRatio = NO_LIGHT;
		writeLightIntensityRatio();
	}

	public void dim() {
		if (dimUp) {
			dimUp();
		} else {
			dimDown();
		}
		lastLightIntensityRatio = lightIntensityRatio;
		writeLightIntensityRatio();
	}

	public boolean isOff() {
		return lightIntensityRatio == NO_LIGHT;
	}

	private void dimUp() {
		if (lightIntensityRatio == FULL_LIGHT) {
			dimUp = false;
			lightIntensityRatio--;
		} else {
			lightIntensityRatio++;
		}
	}

	private void dimDown() {
		if (lightIntensityRatio == NO_LIGHT) {
			dimUp = true;
			lightIntensityRatio++;
		} else {
			lightIntensityRatio--;
		}
	}

	private void writeLightIntensityRatio() {
		lightControl.setLevel(lightIntensityRatio);
	}

}
