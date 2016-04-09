package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class PreOffState extends AbstractState {

	@Inject
	private PreMaxState preMaxState;

	@Inject
	private OffState offState;

	@Inject
	private MasterLightControl masterLightControl;

	@Override
	public void onEnter() {
		super.onEnter();
		sendStartingTimeout();
		masterLightControl.turnOff();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case Timeout:
			nextState = offState;
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
