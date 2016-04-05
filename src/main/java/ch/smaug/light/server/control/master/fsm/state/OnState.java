package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class OnState extends AbstractState {

	@Inject
	private StoppingState stoppingState;

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event) {
		case PositiveEdge:
			nextState = stoppingState;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
