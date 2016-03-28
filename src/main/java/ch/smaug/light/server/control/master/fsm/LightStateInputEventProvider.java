package ch.smaug.light.server.control.master.fsm;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;
import ch.smaug.light.server.pi.KeyButtonEvent;

public class LightStateInputEventProvider {

	@Inject
	private Event<LightStateInputEvent> lightStateInputEvent;

	public void processKeyButtonEvent(@Observes final KeyButtonEvent keyButtonEvent) {
		lightStateInputEvent.fire(createKeyButtonEvent(keyButtonEvent));
	}

	public static LightStateInputEvent createKeyButtonEvent(final KeyButtonEvent keyButtonEvent) {
		LightStateInputEvent result;
		switch (keyButtonEvent.getEdge()) {
		case Negative:
			result = new LightStateInputEvent(InputEventType.NegativeEdge);
			break;
		case Positive:
			result = new LightStateInputEvent(InputEventType.PositiveEdge);
			break;
		default:
			throw new IllegalArgumentException("Unknown type of event: " + keyButtonEvent);
		}
		return result;
	}

}
