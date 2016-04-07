package ch.smaug.light.server.control.master.fsm.event;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.machine.KeyLock;
import ch.smaug.light.server.pi.KeyButtonEvent;

@ApplicationScoped
public class LightStateInputEventProvider {

	@Inject
	private Event<LightStateInputEvent> lightStateInputEvent;

	@Inject
	private KeyLock keyLock;

	public void processKeyButtonEvent(@Observes final KeyButtonEvent keyButtonEvent) {
		if (checkKeyLocked(keyButtonEvent)) {
			lightStateInputEvent.fire(createKeyButtonEvent(keyButtonEvent));
		}
	}

	private boolean checkKeyLocked(final KeyButtonEvent keyButtonEvent) {
		final Optional<String> lockName = keyLock.getLockedKey();
		return !lockName.isPresent() || lockName.get().equals(keyButtonEvent.getKey().name());
	}

	public static LightStateInputEvent createKeyButtonEvent(final KeyButtonEvent keyButtonEvent) {
		LightStateInputEvent result;
		switch (keyButtonEvent.getEdge()) {
		case Negative:
			result = LightStateInputEvent.createNegativeEdgeEvent(keyButtonEvent.getKey().name());
			break;
		case Positive:
			result = LightStateInputEvent.createPositiveEdgeEvent(keyButtonEvent.getKey().name());
			break;
		default:
			throw new IllegalArgumentException("Unknown type of event: " + keyButtonEvent);
		}
		return result;
	}

}
