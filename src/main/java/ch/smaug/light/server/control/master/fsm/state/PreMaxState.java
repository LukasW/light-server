package ch.smaug.light.server.control.master.fsm.state;

import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

public class PreMaxState extends AbstractState {
	@Inject
	private DimState dimState;

	@Inject
	private OnState onState;

	@Inject
	private MasterLightControl masterLightControl;

	@Override
	public void onEnter() {
		super.onEnter();
		sendStartingTimeout();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case NegativeEdge:
			masterLightControl.fullLight();
			nextState = onState;
			break;
		case Timeout:
			nextState = dimState;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
