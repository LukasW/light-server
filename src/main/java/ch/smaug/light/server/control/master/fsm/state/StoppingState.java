package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class StoppingState extends AbstractState {

	@Inject
	private DimState dimState;

	@Inject
	private PreOffState preOffState;

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case NegativeEdge:
			nextState = preOffState;
			break;
		case Timeout:
			nextState = dimState;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}

	@Override
	protected void onEnter() {
		super.onEnter();
		sendStartingTimeout();
	}
}
