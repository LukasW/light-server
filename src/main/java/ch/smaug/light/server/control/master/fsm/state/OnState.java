package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent.Type;

public class OnState implements State {

	@Inject
	private Event<LightStateOutputEvent> lightStateOutputEvent;

	@Inject
	private OffState offState;

	@Override
	public State process(final LightStateInputEvent event) {
		lightStateOutputEvent.fire(new LightStateOutputEvent(Type.TurnOff));
		return offState;
	}

}
