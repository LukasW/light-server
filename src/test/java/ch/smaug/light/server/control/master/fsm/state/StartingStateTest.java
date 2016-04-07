
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

@RunWith(MockitoJUnitRunner.class)
public class StartingStateTest extends AbstractStateTest<StartingState> {

	@InjectMocks
	final StartingState testee = new StartingState();

	@Mock
	private DimState dimState;

	@Mock
	private PreOnState preOnState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_timeOut_dimState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createTimeoutEvent());
		// assert
		assertThat(nextState, is(equalTo(dimState)));
	}

	@Test
	public void processEvent_negativeEdge_preOnState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(preOnState)));
	}

	@Test
	public void onEnter_startStartingTimeoutAndTurnOnLight() {
		// arrange
		// act
		testee.onEnter();
		// verify
		assertSendDeferredEvent(TEST_STARTING_TIMEOUT, LightStateInputEvent.createTimeoutEvent());
		verify(masterLightControl).turnOn();
	}

	@Override
	protected StartingState getTestee() {
		return testee;
	}
}