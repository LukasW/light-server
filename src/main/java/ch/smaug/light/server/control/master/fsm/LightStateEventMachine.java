package ch.smaug.light.server.control.master.fsm;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.master.fsm.state.OffState;
import ch.smaug.light.server.control.master.fsm.state.State;

@ApplicationScoped
public class LightStateEventMachine {

	private final static Logger LOG = LoggerFactory.getLogger(LightStateEventMachine.class);

	private State state;

	@Inject
	private OffState initialState;

	@PostConstruct
	public void setInitialState() {
		state = initialState;
	}

	public synchronized void processEvent(@Observes final LightStateInputEvent event) {
		LOG.debug("Process Event: {}", event);
		state = state.process(event);
	}

	void setState(final State state) {
		this.state = state;
	}
}
