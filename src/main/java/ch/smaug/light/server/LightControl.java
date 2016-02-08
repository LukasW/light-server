package ch.smaug.light.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class LightControl {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	private int value;
	private final static LightControl INSTANCE = new LightControl();

	private final GpioPinPwmOutput out;

	public static LightControl getLightControl() {
		return INSTANCE;
	}

	private LightControl() {
		final GpioController gpio = GpioFactory.getInstance();
		out = gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
		Gpio.pwmSetMode(Gpio.PWM_MODE_BAL);
		Gpio.pwmSetRange(100);
		Gpio.pwmSetClock(10);
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
		out.setPwm(value);
	}
}
