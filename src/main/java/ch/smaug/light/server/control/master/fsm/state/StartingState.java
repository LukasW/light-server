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
public class StartingState implements State {

	@Inject
	private DelayedEvent<LightStateInputEvent> delayLightStateInputEventSender;

	@Inject
	private DimDownState dimDownState;

	@Inject
	private OnState onState;

	@Inject
	private Event<LightStateOutputEvent> lightStateOutputEvent;

	@Override
	public State process(final LightStateInputEvent event) {
		State nextState;
		switch (event.getType()) {
		case NegativeEdge:
			delayLightStateInputEventSender.stopTimer();
			nextState = onState;
			break;
		case Timeout:
			lightStateOutputEvent.fire(new LightStateOutputEvent(Type.Previous));
			delayLightStateInputEventSender.startTimer(20L, new LightStateInputEvent(InputEventType.Timeout));
			nextState = dimDownState;
			break;
		default:
			nextState = this;
			break;
		}
		return nextState;
	}

}
