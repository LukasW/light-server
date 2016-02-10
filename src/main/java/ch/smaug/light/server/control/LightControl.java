package ch.smaug.light.server.control;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

import ch.smaug.light.server.rest.LightRestService;

@Singleton
@Named
@Default
public class LightControl {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	private int value;

	private final GpioPinPwmOutput out;

	private final GpioPinDigitalInput myButton;

	@Inject
	private Linearizer linearizer;

	private LightControl() {
		final GpioController gpio = GpioFactory.getInstance();
		out = gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
		Gpio.pwmSetMode(Gpio.PWM_MODE_BAL);
		Gpio.pwmSetRange(1000);
		Gpio.pwmSetClock(100);

		myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP);
		myButton.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				LOG.info(event.getEventType().name() + " GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
				if (event.getState().isLow()) {
					setValue((getValue() + 100) % 1000);
				}
			}

		});
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		if (value < 0 || value > 1000) {
			throw new IllegalArgumentException("Only value between 0 and 1000 allowed.");
		}
		LOG.info("Set value from {} to {}.", this.value, value);
		this.value = value;
		out.setPwm(value);
	}

	public void fade() {
		new Thread() {

			@Override
			public void run() {

				try {
					for (int j = 0; j < 10; j++) {
						for (int i = 0; i < linearizer.getNumberOfLevels(); i++) {
							out.setPwm(linearizer.getValue(i));
							Thread.sleep(30);
						}
						for (int i = linearizer.getNumberOfLevels() - 2; i > 0; i--) {
							out.setPwm(linearizer.getValue(i));
							Thread.sleep(30);
						}
					}
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}