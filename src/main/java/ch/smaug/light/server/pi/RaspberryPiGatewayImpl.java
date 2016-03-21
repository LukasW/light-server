package ch.smaug.light.server.pi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

@ApplicationScoped
public class RaspberryPiGatewayImpl implements RaspberryPiGateway {

	private static final int PWM_RANGE = 1000;

	private final GpioPinPwmOutput pwmOutput;

	private final GpioPinDigitalInput myButton;

	private final GpioController gpioController;

	@Inject
	private Event<GpioPinDigitalStateChangeEvent> gpioPinDigitalStateChangeEvent;

	RaspberryPiGatewayImpl() {
		gpioController = GpioFactory.getInstance();
		pwmOutput = gpioController.provisionPwmOutputPin(RaspiPin.GPIO_01);
		Gpio.pwmSetMode(Gpio.PWM_MODE_BAL);
		Gpio.pwmSetRange(PWM_RANGE);
		Gpio.pwmSetClock(100);

		myButton = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP);
		myButton.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				gpioPinDigitalStateChangeEvent.fire(event);
			}

		});
	}

	@Override
	public void setPwm(final int value) {
		if (value < 0 || value > PWM_RANGE) {
			throw new IllegalArgumentException(String.format("value must be in in [0..%d]", PWM_RANGE));
		}
		pwmOutput.setPwm(value);
	}
}
