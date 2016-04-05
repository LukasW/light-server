package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class PreOnState extends AbstractState {

	@Inject
	private PreMaxState preMaxState;

	@Inject
	private OnState onState;

	@Override
	public void onEnter() {
		super.onEnter();
		sendStartingTimeout();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event) {
		case Timeout:
			nextState = onState;
			break;
		case PositiveEdge:
			nextState = preMaxState;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
