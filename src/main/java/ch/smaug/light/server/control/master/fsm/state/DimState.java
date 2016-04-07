package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@ApplicationScoped
public class DimState extends AbstractState {

	@Inject
	private OnState onState;
	@Inject
	private OffState offState;

	@Inject
	private MasterLightControl masterLightControl;

	@Override
	public void onEnter() {
		super.onEnter();
		sendRepeatingTimeout();
		masterLightControl.dim();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case NegativeEdge:
			if (masterLightControl.isOff()) {
				nextState = offState;
			} else {
				nextState = onState;
			}
			break;
		case Timeout:
			nextState = this;
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
