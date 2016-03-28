package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.DelayedEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent.Type;

@ApplicationScoped
public class OffState implements State {

	@Inject
	private DelayedEvent<LightStateInputEvent> delayLightStateInputEventSender;

	@Inject
	private StartingState startingState;

	@Inject
	private Event<LightStateOutputEvent> lightStateOutputEvent;

	@Override
	public State process(final LightStateInputEvent event) {
		State nextState;
		switch (event.getType()) {
		case PositiveEdge:
		case Timeout:
			lightStateOutputEvent.fire(new LightStateOutputEvent(Type.TurnOn));
			delayLightStateInputEventSender.startTimer(10L, new LightStateInputEvent(InputEventType.Timeout));
			nextState = startingState;
			break;
		default:
			nextState = this;
		}
		return nextState;
	}
}
