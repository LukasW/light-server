package ch.smaug.light.server.pi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

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

import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;

@ApplicationScoped
public class RaspberryPiGatewayImpl implements RaspberryPiGateway {

	private final static Logger LOG = LoggerFactory.getLogger(RaspberryPiGatewayImpl.class);

	private static final int PWM_RANGE = 1000;

	private final GpioPinPwmOutput pwmOutput;

	private final GpioPinDigitalInput button1;

	private final GpioPinDigitalInput button2;

	private final GpioController gpioController;

	@Inject
	private Instance<Event<KeyButtonEvent>> keyButtonEvent;

	RaspberryPiGatewayImpl() {
		gpioController = GpioFactory.getInstance();
		pwmOutput = gpioController.provisionPwmOutputPin(RaspiPin.GPIO_01);
		Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
		Gpio.pwmSetRange(PWM_RANGE);
		Gpio.pwmSetClock(100);

		button1 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_UP);
		button1.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				LOG.debug("Got Gpio Event: Pin={}, AbstractState={}", event.getPin().getName(), event.getState().getName());
				keyButtonEvent.get().fire(new KeyButtonEvent(Key.Key1, event.getState().isHigh() ? Edge.Negative : Edge.Positive));
			}
		});

		button2 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_UP);
		button2.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				LOG.debug("Got Gpio Event: Pin={}, AbstractState={}", event.getPin().getName(), event.getState().getName());
				keyButtonEvent.get().fire(new KeyButtonEvent(Key.Key2, event.getState().isHigh() ? Edge.Negative : Edge.Positive));
			}
		});
	}

	@Override
	public void setPwm(final int value) {
		LOG.debug("Set PWM to {}", value);
		if (value < 0 || value > PWM_RANGE) {
			throw new IllegalArgumentException(String.format("value must be in in [0..%d]", PWM_RANGE));
		}
		pwmOutput.setPwm(value);
	}
}
