package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

@ApplicationScoped
public class MasterLightControl {

	private final static Logger LOG = LoggerFactory.getLogger(MasterLightControl.class);

	private static final int TURN_ON_PERCENTAGE = 100;
	private static final int TURN_OFF_PERCENTAGE = 0;

	@Inject
	private LinearizedLightControl lightControl;

	private int lightPercentage = 0;

	public void consume(@Observes final LightStateOutputEvent lightStateEvent) {
		LOG.debug("Got event: {}", lightStateEvent);
		switch (lightStateEvent) {
		case TurnOff:
			turnOff();
			break;
		case TurnOn:
			turnOn();
			break;
		case DimDown:
			dimDown();
			break;
		case DimUp:
			dimUp();
			break;
		}
	}

	private void turnOn() {
		lightPercentage = TURN_ON_PERCENTAGE;
		writePercentage();
	}

	private void turnOff() {
		lightPercentage = TURN_OFF_PERCENTAGE;
		writePercentage();
	}

	private void dimUp() {
		lightPercentage = (lightPercentage + 1) % 101;
		writePercentage();
	}

	private void dimDown() {
		lightPercentage = (lightPercentage + 100) % 101;
		writePercentage();
	}

	private void writePercentage() {
		lightControl.setLevel(lightPercentage);
	}
}
