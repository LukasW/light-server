package ch.smaug.light.server.control.master.fsm.machine;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.state.AbstractState;
import ch.smaug.light.server.control.master.fsm.state.OffState;

@ApplicationScoped
public class LightStateEventMachine {

	private final static Logger LOG = LoggerFactory.getLogger(LightStateEventMachine.class);

	private AbstractState state;

	@Inject
	private OffState initialState;

	@Inject
	private MasterLightControl masterLightControl;

	public void initialize() {
		masterLightControl.turnOff();
	}

	@PostConstruct
	public void setInitialState() {
		state = initialState;
	}

	public synchronized void processEvent(@Observes final LightStateInputEvent event) {
		LOG.debug("[{}] Processing event: {}", state.getName(), event.getType().name());
		state.exit();
		AbstractState newState = state.process(event);
		if (newState == null) {
			logUnexpectedEvent(state, event);
			newState = state;
		}
		newState.enter();
		state = newState;
	}

	private void logUnexpectedEvent(final AbstractState state, final LightStateInputEvent event) {
		LOG.warn("[{}] Unexpected event: {}", state.getName(), event.getType().name());
	}

	void setState(final AbstractState state) {
		this.state = state;
	}

}
