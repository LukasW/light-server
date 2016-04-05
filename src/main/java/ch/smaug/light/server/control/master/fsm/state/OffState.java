package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class OffState extends AbstractState {

	@Inject
	private StartingState startingState;

	@Inject
	private MasterLightControl masterLightControl;

	@Override
	public void onEnter() {
		super.onEnter();
		masterLightControl.turnOff();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event) {
		case PositiveEdge:
			nextState = startingState;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
