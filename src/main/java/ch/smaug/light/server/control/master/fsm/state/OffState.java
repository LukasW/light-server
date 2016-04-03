package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import ch.smaug.light.server.cdi.ConfigValue;
import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

@ApplicationScoped
public class OffState extends State {

	@Inject
	private DeferredEvent<LightStateInputEvent> delayLightStateInputEventSender;

	@Inject
	private StartingState startingState;

	@Inject
	private Event<LightStateOutputEvent> lightStateOutputEvent;

	@Inject
	@ConfigValue("startingTimeout")
	private Long startingTimeout;

	@Override
	public State process(final LightStateInputEvent event) {
		State nextState;
		switch (event) {
		case PositiveEdge:
			lightStateOutputEvent.fire(LightStateOutputEvent.TurnOn);
			delayLightStateInputEventSender.sendDeferred(startingTimeout, LightStateInputEvent.Timeout);
			nextState = startingState;
			break;
		default:
			logUnexpectedEvent(event);
			nextState = this;
		}
		return nextState;
	}
}
