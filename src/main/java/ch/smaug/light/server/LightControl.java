package ch.smaug.light.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

public class LightControl {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	private int value;
	private final static LightControl INSTANCE = new LightControl();

	private static final int PIN_NUMBER = 1;

	public static LightControl getLightControl() {
		return INSTANCE;
	}

	private LightControl() {
		Gpio.wiringPiSetup();
		SoftPwm.softPwmCreate(PIN_NUMBER, 0, 100);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		if (value < 0 || value > 100) {
			throw new IllegalArgumentException("Only value between 0 and 100 allowed.");
		}
		LOG.info("Set value from {} to {}.", this.value, value);
		this.value = value;
		SoftPwm.softPwmWrite(PIN_NUMBER, value);
	}
}
