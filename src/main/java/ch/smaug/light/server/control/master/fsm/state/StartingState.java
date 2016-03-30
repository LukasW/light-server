package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import ch.smaug.light.server.cdi.ConfigValue;
import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

@ApplicationScoped
public class StartingState implements State {

	@Inject
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	@Inject
	private DimDownState dimDownState;

	@Inject
	private OnState onState;

	@Inject
	private Event<LightStateOutputEvent> lightStateOutputEvent;

	@Inject
	@ConfigValue("repeatingTimeout")
	private Long repeatingTimeout;

	@Override
	public State process(final LightStateInputEvent event) {
		State nextState;
		switch (event) {
		case NegativeEdge:
			delayedLightStateInputEventSender.cancelAll();
			nextState = onState;
			break;
		case Timeout:
			lightStateOutputEvent.fire(LightStateOutputEvent.DimDown);
			delayedLightStateInputEventSender.sendDeferred(repeatingTimeout, LightStateInputEvent.Timeout);
			nextState = dimDownState;
			break;
		default:
			logUnexpectedEvent(event);
			nextState = this;
			break;
		}
		return nextState;
	}

}
