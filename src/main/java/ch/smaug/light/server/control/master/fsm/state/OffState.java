package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputKeyEvent;
import ch.smaug.light.server.control.master.fsm.machine.KeyLock;

@ApplicationScoped
public class OffState extends AbstractState {

	@Inject
	private StartingState startingState;

	@Inject
	private MasterLightControl masterLightControl;

	@Inject
	private KeyLock keyLock;

	@Override
	public void onEnter() {
		super.onEnter();
		masterLightControl.turnOff();
		keyLock.releaseLock();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case PositiveEdge:
			nextState = startingState;
			keyLock.acquireLock(((LightStateInputKeyEvent) event).getKeyName());
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
