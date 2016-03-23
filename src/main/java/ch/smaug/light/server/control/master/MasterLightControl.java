package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;

@ApplicationScoped
public class MasterLightControl {

	private static final int TURN_ON_PERCENTAGE = 100;
	private static final int TURN_OFF_PERCENTAGE = 0;

	@Inject
	private LinearizedLightControl lightControl;

	private void turnOn() {
		lightControl.setLevel(TURN_ON_PERCENTAGE);
	}

	private void turnOff() {
		lightControl.setLevel(TURN_OFF_PERCENTAGE);
	}

	public void consume(@Observes final LightStateEvent lightStateEvent) {
		switch (lightStateEvent.getType()) {
		case TurnOff:
			turnOff();
			break;
		case TurnOn:
			turnOn();
			break;
		}
	}
}
