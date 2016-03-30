package ch.smaug.light.server.control.master.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

public interface State {

	State process(LightStateInputEvent event);

	default void logUnexpectedEvent(final LightStateInputEvent event) {
		final Logger log = LoggerFactory.getLogger(OffState.class);
		log.warn("[{}] Unexpected event: {}", this.getClass().getSimpleName(), event.name());
	}
}
