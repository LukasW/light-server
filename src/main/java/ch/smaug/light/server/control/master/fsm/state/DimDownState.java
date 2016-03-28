package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;

@ApplicationScoped
public class DimDownState implements State {

	@Override
	public State process(final LightStateInputEvent event) {
		return this;
	}

}
