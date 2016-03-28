package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent;

@ApplicationScoped
public class MasterLightControl {

	private final static Logger LOG = LoggerFactory.getLogger(MasterLightControl.class);

	private static final int TURN_ON_PERCENTAGE = 100;
	private static final int TURN_OFF_PERCENTAGE = 0;

	@Inject
	private LinearizedLightControl lightControl;

	public void consume(@Observes final LightStateOutputEvent lightStateEvent) {
		LOG.debug("Got event: {}", lightStateEvent);
		switch (lightStateEvent.getType()) {
		case TurnOff:
			turnOff();
			break;
		case TurnOn:
			turnOn();
			break;
		case Previous:
			previous();
			break;
		}
	}

	private void turnOn() {
		lightControl.setLevel(TURN_ON_PERCENTAGE);
	}

	private void turnOff() {
		lightControl.setLevel(TURN_OFF_PERCENTAGE);
	}

	private void previous() {
		lightControl.setLevel(80);
	}
}
