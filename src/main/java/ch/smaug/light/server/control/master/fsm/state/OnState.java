package ch.smaug.light.server.control.master.fsm.state;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputKeyEvent;
import ch.smaug.light.server.control.master.fsm.machine.KeyLock;

@ApplicationScoped
public class OnState extends AbstractState {

	@Inject
	private StoppingState stoppingState;

	@Inject
	private KeyLock keyLock;

	@Override
	protected void onEnter() {
		super.onEnter();
		keyLock.releaseLock();
	}

	@Override
	public AbstractState process(final LightStateInputEvent event) {
		AbstractState nextState;
		switch (event.getType()) {
		case PositiveEdge:
			nextState = stoppingState;
			keyLock.acquireLock(((LightStateInputKeyEvent) event).getKeyName());
			break;
		default:
			nextState = null;
		}
		return nextState;
	}
}
