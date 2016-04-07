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

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.machine.KeyLock;

@RunWith(MockitoJUnitRunner.class)
public class OffStateTest extends AbstractStateTest<OffState> {

	@InjectMocks
	private final OffState testee = new OffState();

	@Mock
	private StartingState startingState;

	@Mock
	private MasterLightControl masterLightControl;

	@Mock
	private KeyLock keyLock;

	@Test
	public void processEvent_positiveEdge_startingStateAndLockKey() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createPositiveEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(startingState)));
		verify(keyLock).acquireLock("Key1");
	}

	@Test
	public void onEnter_turnLightOffAndReleaseKeyLock() {
		// arrange
		// act
		testee.onEnter();
		// assert
		verify(masterLightControl).turnOff();
		verify(keyLock).releaseLock();
	}

	@Override
	protected OffState getTestee() {
		return testee;
	}
}
