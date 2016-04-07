package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class StartingState extends AbstractState {

	@Inject
	private DimState dimState;

	@Inject
	private PreOnState preOnState;

	@Inject
	private MasterLightControl masterLightControl;

	@Override
	public void onEnter() {
		super.onEnter();
		masterLightControl.turnOn();
		sendStartingTimeout();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case NegativeEdge:
			nextState = preOnState;
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
