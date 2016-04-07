package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.machine.KeyLock;

@RunWith(MockitoJUnitRunner.class)
public class OnStateTest extends AbstractStateTest<OnState> {

	@InjectMocks
	private final OnState testee = new OnState();

	@Mock
	private StoppingState stoppingState;

	@Mock
	private KeyLock keyLock;

	@Test
	public void processEvent_positiveEdge_stoppingStateAndLockKey() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createPositiveEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(stoppingState)));
		verify(keyLock).acquireLock("Key1");
	}

	@Test
	public void onEnter_startStartingTimeoutAndDim() {
		// arrange
		// act
		testee.onEnter();
		// verify
		verify(keyLock).releaseLock();
	}

	@Override
	protected OnState getTestee() {
		return testee;
	}
}