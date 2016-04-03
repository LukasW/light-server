package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.cdi.ConfigValue;
import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class OnState extends State {

	@Inject
	private DeferredEvent<LightStateInputEvent> delayLightStateInputEventSender;

	@Inject
	private PressedState pressedState;

	@Inject
	@ConfigValue("startingTimeout")
	private Long startingTimeout;

	@Override
	public State process(final LightStateInputEvent event) {
		State nextState;
		switch (event) {
		case PositiveEdge:
			delayLightStateInputEventSender.sendDeferred(startingTimeout, LightStateInputEvent.Timeout);
			nextState = pressedState;
			break;
		default:
			logUnexpectedEvent(event);
			nextState = this;
		}
		return nextState;
	}
}
